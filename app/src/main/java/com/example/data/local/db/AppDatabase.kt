package com.example.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        WaterResourceEntity::class,
        FoodItemEntity::class,
        MedicineEntity::class,
        PowerResourceEntity::class,
        FuelResourceEntity::class,
        SafeLocationEntity::class,
        EmergencyAssessmentEntity::class,
        ChecklistItemEntity::class,
        EmergencyContactEntity::class,
        MapRegionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun resourceDao(): ResourceDao
    abstract fun safeLocationDao(): SafeLocationDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun emergencyContactDao(): EmergencyContactDao
    abstract fun mapRegionDao(): MapRegionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jeevansetu_disaster_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Prepopulate default data in background coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                prepopulateDatabase(database)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateDatabase(db: AppDatabase) {
            // 1. Initial User Profile (Onboarding pending)
            db.userProfileDao().insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    numberOfAdults = 1,
                    numberOfChildren = 0,
                    numberOfElderly = 0,
                    numberOfInjured = 0,
                    specialNeeds = "",
                    regionName = "",
                    isSetupCompleted = false,
                    batterySaverMode = false,
                    simulatedGpsLat = 28.6139,
                    simulatedGpsLng = 77.2090
                )
            )

            // 2. Initial Water Resources (0.0 L - Unconfigured until user setup)
            db.resourceDao().updateWaterResource(
                WaterResourceEntity(
                    id = 1,
                    drinkingWaterLiters = 0.0,
                    utilityWaterLiters = 0.0
                )
            )

            // 3. Food Items: Initially empty. User enters actual food reserves during onboarding.

            // 4. Medicines: Initially empty. User configures specific household medical needs.

            // 5. Initial Power Resources (Baseline device values, no assumed backup power)
            db.resourceDao().updatePowerResource(
                PowerResourceEntity(
                    id = 1,
                    phoneBatteryPercent = 50,
                    hasPowerBank = false,
                    powerBankCapacityMah = 0,
                    powerBankPercent = 0,
                    flashlightCount = 0,
                    spareBatteriesCount = 0
                )
            )

            // 6. Initial Fuel Resource (0% - No vehicle assumed until user confirms)
            db.resourceDao().updateFuelResource(
                FuelResourceEntity(
                    id = 1,
                    hasVehicle = false,
                    vehicleType = "None",
                    fuelPercentage = 0,
                    estimatedRangeKm = 0.0,
                    reservedForEvacuation = false
                )
            )

            // 7. Initial Safe Locations
            val safeLocations = listOf(
                SafeLocationEntity(
                    name = "Central High School Emergency Shelter",
                    latitude = 28.6220,
                    longitude = 77.2180,
                    category = "Emergency Shelter",
                    capacity = "800 persons",
                    contactPhone = "1077",
                    elevationMeters = 245,
                    notes = "Designated high-ground building, generator power & emergency drinking tanks",
                    isPreloaded = true,
                    hasOfflineMap = true
                ),
                SafeLocationEntity(
                    name = "District Memorial Trauma Hospital",
                    latitude = 28.6310,
                    longitude = 77.2250,
                    category = "Hospital",
                    capacity = "Full ICU & Trauma Care",
                    contactPhone = "102",
                    elevationMeters = 250,
                    notes = "Emergency triage, helipad, backup medical oxygen reserves",
                    isPreloaded = true,
                    hasOfflineMap = true
                ),
                SafeLocationEntity(
                    name = "Civic Center Disaster Relief Hub",
                    latitude = 28.6050,
                    longitude = 77.2120,
                    category = "Government Building",
                    capacity = "1,500 persons",
                    contactPhone = "1070",
                    elevationMeters = 240,
                    notes = "Official civil defense coordination point & food distribution",
                    isPreloaded = true,
                    hasOfflineMap = true
                ),
                SafeLocationEntity(
                    name = "Northern Hill Elevated Staging Camp",
                    latitude = 28.6450,
                    longitude = 77.1950,
                    category = "Safe Camp",
                    capacity = "Open grounds (5,000+)",
                    contactPhone = "112",
                    elevationMeters = 310,
                    notes = "High elevation safe zone above maximum flood surge line",
                    isPreloaded = true,
                    hasOfflineMap = true
                ),
                SafeLocationEntity(
                    name = "Relative / Family Safe House (East Ridge)",
                    latitude = 28.5980,
                    longitude = 77.2350,
                    category = "Family Safe House",
                    capacity = "Private residence",
                    contactPhone = "+91 98765 43210",
                    elevationMeters = 260,
                    notes = "Concrete roof, solar backup power, spare water tank",
                    isPreloaded = true,
                    hasOfflineMap = true
                )
            )
            db.safeLocationDao().insertAll(safeLocations)

            // 8. Initial Evacuation & Emergency Checklist Items
            val checklist = listOf(
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "Drinking water (at least 3 liters per person)",
                    description = "Sealed bottles or sanitized hydration bladders",
                    isCompleted = true,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "Non-perishable high-calorie food (3 days supply)",
                    description = "Energy bars, dry fruit, crackers, canned rations",
                    isCompleted = true,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "Critical prescription medications & copy of prescriptions",
                    description = "Keep in waterproof ziplock pouch",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "Government IDs, deed/property cards, insurance documents",
                    description = "Store in waterproof document carrier",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "Fully charged power bank & charging cables",
                    description = "Preserve charge for emergency dispatch calls",
                    isCompleted = true,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "LED Flashlight & spare batteries",
                    description = "Check bulb and switch functionality",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "EVACUATION",
                    title = "Sturdy boots or closed-toe walking shoes",
                    description = "Never walk in flood or rubble barefoot",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "FIRST_AID",
                    title = "Sterile gauze pads & compression bandages",
                    description = "For immediate severe bleeding control",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "FIRST_AID",
                    title = "Antiseptic wipes & antibiotic ointment",
                    description = "Clean lacerations immediately to prevent contamination",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "HOME_SHELTER",
                    title = "Turn off main electrical breaker if water enters",
                    description = "Prevents electrocution hazard through standing water",
                    isCompleted = false,
                    isEssential = true
                ),
                ChecklistItemEntity(
                    category = "HOME_SHELTER",
                    title = "Shut off main gas valve if seismic tremor occurred",
                    description = "Do not light matches or flick switches if gas is smelled",
                    isCompleted = false,
                    isEssential = true
                )
            )
            db.checklistDao().insertAll(checklist)

            // 9. Initial Emergency Contacts
            val contacts = listOf(
                EmergencyContactEntity(
                    name = "National Emergency Response Number",
                    phoneNumber = "112",
                    relationship = "Disaster Helpline",
                    isPrimary = true
                ),
                EmergencyContactEntity(
                    name = "Disaster Relief Control Room (NDRF)",
                    phoneNumber = "1078",
                    relationship = "Local Rescue",
                    isPrimary = true
                ),
                EmergencyContactEntity(
                    name = "Medical Ambulance Emergency",
                    phoneNumber = "102",
                    relationship = "Ambulance",
                    isPrimary = true
                ),
                EmergencyContactEntity(
                    name = "Fire & Rescue Services",
                    phoneNumber = "101",
                    relationship = "Local Rescue",
                    isPrimary = false
                ),
                EmergencyContactEntity(
                    name = "Family Emergency Contact (Uncle Rajesh)",
                    phoneNumber = "+91 98110 99887",
                    relationship = "Family",
                    isPrimary = false
                )
            )
            db.emergencyContactDao().insertAll(contacts)

            // 10. Initial Offline Map Regions
            val regions = listOf(
                MapRegionEntity(
                    regionName = "Metro Capital District & Ridge",
                    centerLat = 28.6139,
                    centerLng = 77.2090,
                    radiusKm = 25.0,
                    sizeMb = 42.5,
                    tileCount = 1450,
                    isDownloaded = true,
                    downloadProgress = 100,
                    downloadDate = System.currentTimeMillis() - 86400000L
                ),
                MapRegionEntity(
                    regionName = "Coastal Valley & Delta Basin",
                    centerLat = 13.0827,
                    centerLng = 80.2707,
                    radiusKm = 30.0,
                    sizeMb = 58.2,
                    tileCount = 1890,
                    isDownloaded = false,
                    downloadProgress = 0,
                    downloadDate = 0L
                ),
                MapRegionEntity(
                    regionName = "Northern Himalayan Hill & River Sector",
                    centerLat = 30.3165,
                    centerLng = 78.0322,
                    radiusKm = 20.0,
                    sizeMb = 35.8,
                    tileCount = 1120,
                    isDownloaded = false,
                    downloadProgress = 0,
                    downloadDate = 0L
                )
            )
            db.mapRegionDao().insertAll(regions)
        }
    }
}
