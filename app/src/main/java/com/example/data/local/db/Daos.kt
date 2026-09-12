package com.example.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}

@Dao
interface ResourceDao {
    // Water
    @Query("SELECT * FROM water_resource WHERE id = 1")
    fun getWaterResource(): Flow<WaterResourceEntity?>

    @Query("SELECT * FROM water_resource WHERE id = 1")
    suspend fun getWaterResourceOnce(): WaterResourceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWaterResource(water: WaterResourceEntity)

    // Food
    @Query("SELECT * FROM food_items ORDER BY isPerishable DESC, daysUntilExpiry ASC")
    fun getAllFoodItems(): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_items ORDER BY isPerishable DESC, daysUntilExpiry ASC")
    suspend fun getAllFoodItemsOnce(): List<FoodItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(item: FoodItemEntity): Long

    @Update
    suspend fun updateFoodItem(item: FoodItemEntity)

    @Delete
    suspend fun deleteFoodItem(item: FoodItemEntity)

    @Query("DELETE FROM food_items")
    suspend fun clearAllFoodItems()

    // Medicine
    @Query("SELECT * FROM medicine_items ORDER BY isCritical DESC, daysRemaining ASC")
    fun getAllMedicines(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicine_items ORDER BY isCritical DESC, daysRemaining ASC")
    suspend fun getAllMedicinesOnce(): List<MedicineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(item: MedicineEntity): Long

    @Update
    suspend fun updateMedicine(item: MedicineEntity)

    @Delete
    suspend fun deleteMedicine(item: MedicineEntity)

    @Query("DELETE FROM medicine_items")
    suspend fun clearAllMedicines()

    // Power
    @Query("SELECT * FROM power_resource WHERE id = 1")
    fun getPowerResource(): Flow<PowerResourceEntity?>

    @Query("SELECT * FROM power_resource WHERE id = 1")
    suspend fun getPowerResourceOnce(): PowerResourceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePowerResource(power: PowerResourceEntity)

    // Fuel
    @Query("SELECT * FROM fuel_resource WHERE id = 1")
    fun getFuelResource(): Flow<FuelResourceEntity?>

    @Query("SELECT * FROM fuel_resource WHERE id = 1")
    suspend fun getFuelResourceOnce(): FuelResourceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFuelResource(fuel: FuelResourceEntity)
}

@Dao
interface SafeLocationDao {
    @Query("SELECT * FROM safe_locations ORDER BY name ASC")
    fun getAllSafeLocations(): Flow<List<SafeLocationEntity>>

    @Query("SELECT * FROM safe_locations ORDER BY name ASC")
    suspend fun getAllSafeLocationsOnce(): List<SafeLocationEntity>

    @Query("SELECT * FROM safe_locations WHERE id = :id")
    suspend fun getLocationById(id: Long): SafeLocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: SafeLocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<SafeLocationEntity>)

    @Update
    suspend fun updateLocation(location: SafeLocationEntity)

    @Delete
    suspend fun deleteLocation(location: SafeLocationEntity)

    @Query("SELECT COUNT(*) FROM safe_locations")
    suspend fun count(): Int
}

@Dao
interface AssessmentDao {
    @Query("SELECT * FROM assessments ORDER BY timestamp DESC")
    fun getAllAssessments(): Flow<List<EmergencyAssessmentEntity>>

    @Query("SELECT * FROM assessments ORDER BY timestamp DESC LIMIT 1")
    fun getLatestAssessment(): Flow<EmergencyAssessmentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: EmergencyAssessmentEntity): Long

    @Query("DELETE FROM assessments WHERE id = :id")
    suspend fun deleteAssessment(id: Long)
}

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_items ORDER BY isEssential DESC, id ASC")
    fun getAllChecklistItems(): Flow<List<ChecklistItemEntity>>

    @Query("SELECT * FROM checklist_items WHERE category = :category ORDER BY id ASC")
    fun getItemsByCategory(category: String): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChecklistItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ChecklistItemEntity>)

    @Update
    suspend fun updateItem(item: ChecklistItemEntity)

    @Delete
    suspend fun deleteItem(item: ChecklistItemEntity)

    @Query("SELECT COUNT(*) FROM checklist_items")
    suspend fun count(): Int

    @Query("UPDATE checklist_items SET isCompleted = :completed WHERE title LIKE '%' || :keyword || '%'")
    suspend fun updateCompletionByKeyword(keyword: String, completed: Boolean)
}

@Dao
interface EmergencyContactDao {
    @Query("SELECT * FROM emergency_contacts ORDER BY isPrimary DESC, name ASC")
    fun getAllContacts(): Flow<List<EmergencyContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContactEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<EmergencyContactEntity>)

    @Update
    suspend fun updateContact(contact: EmergencyContactEntity)

    @Delete
    suspend fun deleteContact(contact: EmergencyContactEntity)

    @Query("SELECT COUNT(*) FROM emergency_contacts")
    suspend fun count(): Int
}

@Dao
interface MapRegionDao {
    @Query("SELECT * FROM map_regions ORDER BY regionName ASC")
    fun getAllRegions(): Flow<List<MapRegionEntity>>

    @Query("SELECT * FROM map_regions")
    suspend fun getAllRegionsOnce(): List<MapRegionEntity>

    @Query("SELECT * FROM map_regions WHERE isDownloaded = 1")
    fun getDownloadedRegions(): Flow<List<MapRegionEntity>>

    @Query("SELECT * FROM map_regions WHERE id = :id")
    suspend fun getRegionById(id: Long): MapRegionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegion(region: MapRegionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(regions: List<MapRegionEntity>)

    @Update
    suspend fun updateRegion(region: MapRegionEntity)

    @Query("SELECT COUNT(*) FROM map_regions")
    suspend fun count(): Int
}
