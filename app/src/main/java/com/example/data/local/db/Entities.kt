package com.example.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val numberOfAdults: Int = 2,
    val numberOfChildren: Int = 1,
    val numberOfElderly: Int = 0,
    val numberOfInjured: Int = 0,
    val specialNeeds: String = "",
    val regionName: String = "Metro Disaster Prep Zone",
    val isSetupCompleted: Boolean = false,
    val batterySaverMode: Boolean = false,
    val simulatedGpsLat: Double = 28.6139,
    val simulatedGpsLng: Double = 77.2090,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "water_resource")
data class WaterResourceEntity(
    @PrimaryKey val id: Int = 1,
    val drinkingWaterLiters: Double = 16.0,
    val utilityWaterLiters: Double = 25.0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val estimatedMeals: Int,
    val isPerishable: Boolean,
    val expirationDate: String,
    val daysUntilExpiry: Int,
    val notes: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "medicine_items")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: Int,
    val dailyUsage: Int,
    val daysRemaining: Int,
    val isCritical: Boolean,
    val notes: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "power_resource")
data class PowerResourceEntity(
    @PrimaryKey val id: Int = 1,
    val phoneBatteryPercent: Int = 78,
    val hasPowerBank: Boolean = true,
    val powerBankCapacityMah: Int = 20000,
    val powerBankPercent: Int = 90,
    val flashlightCount: Int = 2,
    val spareBatteriesCount: Int = 6,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "fuel_resource")
data class FuelResourceEntity(
    @PrimaryKey val id: Int = 1,
    val hasVehicle: Boolean = true,
    val vehicleType: String = "Family Car",
    val fuelPercentage: Int = 65,
    val estimatedRangeKm: Double = 320.0,
    val reservedForEvacuation: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "safe_locations")
data class SafeLocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String, // Hospital, Emergency Shelter, Government Building, Family Safe House, Safe Camp, Custom
    val capacity: String = "500 people",
    val contactPhone: String = "",
    val elevationMeters: Int = 220,
    val notes: String = "",
    val isPreloaded: Boolean = false,
    val hasOfflineMap: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "assessments")
data class EmergencyAssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val disasterType: String,
    val riskScore: Int,
    val riskLevel: String,
    val actionType: String,
    val headline: String,
    val recommendation: String,
    val dangerFactorsSummary: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String, // EVACUATION, FIRST_AID, HOME_SHELTER, DOCUMENTS
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val isEssential: Boolean = true
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val relationship: String, // Family, Local Rescue, Police, Ambulance, Disaster Helpline
    val isPrimary: Boolean = false
)

@Entity(tableName = "map_regions")
data class MapRegionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val regionName: String,
    val centerLat: Double,
    val centerLng: Double,
    val radiusKm: Double,
    val sizeMb: Double,
    val tileCount: Int,
    val isDownloaded: Boolean = false,
    val downloadProgress: Int = 0,
    val downloadDate: Long = 0,
    val exportedFilePath: String? = null
)
