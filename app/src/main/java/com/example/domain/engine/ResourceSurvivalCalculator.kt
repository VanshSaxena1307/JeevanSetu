package com.example.domain.engine

import com.example.domain.model.FoodSurvivalEstimate
import com.example.domain.model.FuelSurvivalEstimate
import com.example.domain.model.MedicineSurvivalEstimate
import com.example.domain.model.PowerSurvivalEstimate
import com.example.domain.model.ResourceStatus
import com.example.domain.model.WaterSurvivalEstimate

object ResourceSurvivalCalculator {

    // Configurable survival guidelines:
    // Minimum survival drinking water: ~2.0 Liters per person per day under stress/hot conditions
    private const val LITERS_PER_PERSON_PER_DAY = 2.0
    // Survival rationing: 2 modest meals per person per day
    private const val MEALS_PER_PERSON_PER_DAY = 2.0

    fun calculateWater(
        drinkingLiters: Double,
        utilityLiters: Double,
        totalPeople: Int
    ): WaterSurvivalEstimate {
        val people = totalPeople.coerceAtLeast(1)
        val dailyRequirement = people * LITERS_PER_PERSON_PER_DAY
        val safeDrinking = drinkingLiters.coerceAtLeast(0.0)
        val estimatedDays = if (dailyRequirement > 0) safeDrinking / dailyRequirement else 0.0

        val status = when {
            safeDrinking <= 0.0 -> ResourceStatus.CRITICAL
            estimatedDays < 2.0 -> ResourceStatus.CRITICAL
            estimatedDays < 5.0 -> ResourceStatus.LIMITED
            else -> ResourceStatus.SUFFICIENT
        }

        val recommendation = when (status) {
            ResourceStatus.CRITICAL -> "URGENT: Water reserves critical (< 2 days). Ration drinking strictly to 1.5L/person/day. Avoid salty food. Do not drink floodwater without boiling (min 3 minutes) or chlorine tablets."
            ResourceStatus.LIMITED -> "CAUTION: Water is limited. Restrict utility water for drinking purification if safe. Discontinue unnecessary washing. Collect clean rainwater if available."
            ResourceStatus.SUFFICIENT -> "ADEQUATE: Current drinking reserves can sustain your party for approximately ${String.format("%.1f", estimatedDays)} days. Keep containers tightly capped to prevent insect and bacterial contamination."
        }

        return WaterSurvivalEstimate(
            totalLiters = drinkingLiters + utilityLiters,
            drinkingLiters = drinkingLiters,
            utilityLiters = utilityLiters,
            totalPeople = people,
            estimatedDaysDrinking = estimatedDays,
            status = status,
            recommendation = recommendation
        )
    }

    fun calculateFood(
        totalMeals: Int,
        totalPeople: Int,
        perishableCount: Int,
        nonPerishableCount: Int
    ): FoodSurvivalEstimate {
        val safeMeals = totalMeals.coerceAtLeast(0)
        val people = totalPeople.coerceAtLeast(1)
        val dailyRequirement = people * MEALS_PER_PERSON_PER_DAY
        val estimatedDays = if (dailyRequirement > 0) safeMeals.toDouble() / dailyRequirement else 0.0

        val status = when {
            safeMeals <= 0 -> ResourceStatus.CRITICAL
            estimatedDays < 2.0 -> ResourceStatus.CRITICAL
            estimatedDays < 6.0 -> ResourceStatus.LIMITED
            else -> ResourceStatus.SUFFICIENT
        }

        val recommendation = when {
            safeMeals <= 0 -> "CRITICAL FOOD SHORTAGE: Zero food rations remaining. Adopt emergency hunger protocols and signal emergency rescue."
            perishableCount > 0 -> "CONSUMPTION PRIORITY: You have $perishableCount perishable food items. Consume these first while electricity or refrigeration is disrupted before opening sealed canned/dry goods."
            status == ResourceStatus.CRITICAL -> "CRITICAL FOOD SHORTAGE: Less than 2 days of food remaining. Adopt strict 1-2 meal/day disaster rationing and signal relief teams."
            status == ResourceStatus.LIMITED -> "MODERATE SUPPLY: Approximately ${String.format("%.1f", estimatedDays)} days available. Maintain portion control and safeguard supplies from moisture."
            else -> "STABLE SUPPLY: Food reserves sufficient for approximately ${String.format("%.1f", estimatedDays)} days. Keep canned rations dry and sealed."
        }

        return FoodSurvivalEstimate(
            totalMeals = safeMeals,
            totalPeople = people,
            estimatedDaysRemaining = estimatedDays,
            perishableCount = perishableCount.coerceAtLeast(0),
            nonPerishableCount = nonPerishableCount.coerceAtLeast(0),
            status = status,
            recommendation = recommendation
        )
    }

    fun calculatePower(
        phoneBatteryPercent: Int,
        hasPowerBank: Boolean,
        powerBankPercent: Int,
        isBatterySaverActive: Boolean
    ): PowerSurvivalEstimate {
        val safeBattery = phoneBatteryPercent.coerceIn(0, 100)
        val safeBankPercent = powerBankPercent.coerceIn(0, 100)
        // Base estimation: 100% phone battery lasts ~14 hours normal, ~36 hours ultra battery saver
        val normalHours = (safeBattery * 0.14).toInt()
        val ecoHours = (safeBattery * 0.36).toInt() + (if (hasPowerBank) (safeBankPercent * 0.45).toInt() else 0)

        val status = when {
            safeBattery <= 20 -> ResourceStatus.CRITICAL
            safeBattery <= 45 -> ResourceStatus.LIMITED
            else -> ResourceStatus.SUFFICIENT
        }

        val tips = mutableListOf<String>()
        if (safeBattery < 30 || !isBatterySaverActive) {
            tips.add("Enable system Battery Saver mode immediately")
            tips.add("Reduce display brightness to minimum legible level")
            tips.add("Disable Bluetooth, Location (continuous), and Wi-Fi hotspot")
            tips.add("Avoid streaming video, social media, or gaming")
            tips.add("Check messages in 15-minute bursts once every 2-3 hours to preserve charge")
        } else {
            tips.add("Battery Saver is active. Keep screen timeout set to 15-30 seconds")
            tips.add("Keep device sheltered from extreme temperatures to prevent battery drain")
        }

        return PowerSurvivalEstimate(
            phoneBatteryPercent = safeBattery,
            powerBankAvailable = hasPowerBank,
            estimatedHoursNormal = normalHours,
            estimatedHoursEco = ecoHours,
            status = status,
            conservationTips = tips
        )
    }

    fun calculateFuel(
        hasVehicle: Boolean,
        fuelPercent: Int,
        targetDistanceKm: Double?
    ): FuelSurvivalEstimate {
        if (!hasVehicle) {
            return FuelSurvivalEstimate(
                hasVehicle = false,
                fuelPercent = 0,
                estimatedRangeKm = 0.0,
                canReachSafeLocation = false,
                status = ResourceStatus.CRITICAL
            )
        }

        val safeFuel = fuelPercent.coerceIn(0, 100)
        // Average compact vehicle has ~450 km full tank range
        val estimatedRange = (safeFuel / 100.0) * 450.0
        val target = targetDistanceKm ?: 15.0
        val canReach = estimatedRange >= (target * 2.0) // factoring in traffic jams and detours

        val status = when {
            safeFuel <= 15 -> ResourceStatus.CRITICAL
            safeFuel <= 35 -> ResourceStatus.LIMITED
            else -> ResourceStatus.SUFFICIENT
        }

        return FuelSurvivalEstimate(
            hasVehicle = true,
            fuelPercent = safeFuel,
            estimatedRangeKm = estimatedRange,
            canReachSafeLocation = canReach,
            status = status
        )
    }
}
