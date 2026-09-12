package com.example.data.repository

import com.example.data.local.db.AppDatabase
import com.example.data.local.db.ChecklistItemEntity
import com.example.data.local.db.EmergencyAssessmentEntity
import com.example.data.local.db.EmergencyContactEntity
import com.example.data.local.db.FoodItemEntity
import com.example.data.local.db.FuelResourceEntity
import com.example.data.local.db.MapRegionEntity
import com.example.data.local.db.MedicineEntity
import com.example.data.local.db.PowerResourceEntity
import com.example.data.local.db.SafeLocationEntity
import com.example.data.local.db.UserProfileEntity
import com.example.data.local.db.WaterResourceEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(private val database: AppDatabase) {

    // User Profile
    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getUserProfile()
    suspend fun getUserProfileOnce(): UserProfileEntity? = database.userProfileDao().getUserProfileOnce()
    suspend fun updateProfile(profile: UserProfileEntity) = database.userProfileDao().insertOrUpdateProfile(profile)

    // Water
    val waterResource: Flow<WaterResourceEntity?> = database.resourceDao().getWaterResource()
    suspend fun getWaterResourceOnce(): WaterResourceEntity? = database.resourceDao().getWaterResourceOnce()
    suspend fun updateWater(water: WaterResourceEntity) = database.resourceDao().updateWaterResource(water)

    // Food
    val foodItems: Flow<List<FoodItemEntity>> = database.resourceDao().getAllFoodItems()
    suspend fun getAllFoodItemsOnce(): List<FoodItemEntity> = database.resourceDao().getAllFoodItemsOnce()
    suspend fun addFoodItem(item: FoodItemEntity): Long = database.resourceDao().insertFoodItem(item)
    suspend fun updateFoodItem(item: FoodItemEntity) = database.resourceDao().updateFoodItem(item)
    suspend fun deleteFoodItem(item: FoodItemEntity) = database.resourceDao().deleteFoodItem(item)
    suspend fun clearAllFoodItems() = database.resourceDao().clearAllFoodItems()

    // Medicines
    val medicines: Flow<List<MedicineEntity>> = database.resourceDao().getAllMedicines()
    suspend fun getAllMedicinesOnce(): List<MedicineEntity> = database.resourceDao().getAllMedicinesOnce()
    suspend fun addMedicine(item: MedicineEntity): Long = database.resourceDao().insertMedicine(item)
    suspend fun updateMedicine(item: MedicineEntity) = database.resourceDao().updateMedicine(item)
    suspend fun deleteMedicine(item: MedicineEntity) = database.resourceDao().deleteMedicine(item)
    suspend fun clearAllMedicines() = database.resourceDao().clearAllMedicines()

    // Power
    val powerResource: Flow<PowerResourceEntity?> = database.resourceDao().getPowerResource()
    suspend fun getPowerResourceOnce(): PowerResourceEntity? = database.resourceDao().getPowerResourceOnce()
    suspend fun updatePower(power: PowerResourceEntity) = database.resourceDao().updatePowerResource(power)

    // Fuel
    val fuelResource: Flow<FuelResourceEntity?> = database.resourceDao().getFuelResource()
    suspend fun getFuelResourceOnce(): FuelResourceEntity? = database.resourceDao().getFuelResourceOnce()
    suspend fun updateFuel(fuel: FuelResourceEntity) = database.resourceDao().updateFuelResource(fuel)

    // Safe Locations
    val safeLocations: Flow<List<SafeLocationEntity>> = database.safeLocationDao().getAllSafeLocations()
    suspend fun getAllSafeLocationsOnce(): List<SafeLocationEntity> = database.safeLocationDao().getAllSafeLocationsOnce()
    suspend fun addSafeLocation(location: SafeLocationEntity): Long = database.safeLocationDao().insertLocation(location)
    suspend fun updateSafeLocation(location: SafeLocationEntity) = database.safeLocationDao().updateLocation(location)
    suspend fun deleteSafeLocation(location: SafeLocationEntity) = database.safeLocationDao().deleteLocation(location)

    // Assessments
    val assessments: Flow<List<EmergencyAssessmentEntity>> = database.assessmentDao().getAllAssessments()
    val latestAssessment: Flow<EmergencyAssessmentEntity?> = database.assessmentDao().getLatestAssessment()
    suspend fun saveAssessment(assessment: EmergencyAssessmentEntity): Long = database.assessmentDao().insertAssessment(assessment)

    // Checklist
    val checklistItems: Flow<List<ChecklistItemEntity>> = database.checklistDao().getAllChecklistItems()
    suspend fun updateChecklistItem(item: ChecklistItemEntity) = database.checklistDao().updateItem(item)
    suspend fun addChecklistItem(item: ChecklistItemEntity): Long = database.checklistDao().insertItem(item)
    suspend fun deleteChecklistItem(item: ChecklistItemEntity) = database.checklistDao().deleteItem(item)
    suspend fun updateChecklistByKeyword(keyword: String, completed: Boolean) =
        database.checklistDao().updateCompletionByKeyword(keyword, completed)

    // Emergency Contacts
    val emergencyContacts: Flow<List<EmergencyContactEntity>> = database.emergencyContactDao().getAllContacts()
    suspend fun addEmergencyContact(contact: EmergencyContactEntity): Long = database.emergencyContactDao().insertContact(contact)
    suspend fun updateEmergencyContact(contact: EmergencyContactEntity) = database.emergencyContactDao().updateContact(contact)
    suspend fun deleteEmergencyContact(contact: EmergencyContactEntity) = database.emergencyContactDao().deleteContact(contact)

    // Map Regions
    val mapRegions: Flow<List<MapRegionEntity>> = database.mapRegionDao().getAllRegions()
    val downloadedRegions: Flow<List<MapRegionEntity>> = database.mapRegionDao().getDownloadedRegions()
    suspend fun getAllMapRegionsOnce(): List<MapRegionEntity> = database.mapRegionDao().getAllRegionsOnce()
    suspend fun addMapRegion(region: MapRegionEntity): Long = database.mapRegionDao().insertRegion(region)
    suspend fun updateMapRegion(region: MapRegionEntity) = database.mapRegionDao().updateRegion(region)
}
