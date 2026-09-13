package com.example

import com.example.domain.engine.ResourceSurvivalCalculator
import com.example.domain.model.ResourceStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceSurvivalCalculatorTest {

    // -------------------------------------------------------------------------
    // Water
    // -------------------------------------------------------------------------

    @Test
    fun water_zeroDrinking_returnsCritical() {
        val est = ResourceSurvivalCalculator.calculateWater(0.0, 0.0, 2)
        assertEquals(ResourceStatus.CRITICAL, est.status)
        assertEquals(0.0, est.estimatedDaysDrinking, 0.001)
    }

    @Test
    fun water_sufficientFor7Days_returnsSufficient() {
        // 2 people x 2L/day = 4L/day -> 28L = 7 days
        val est = ResourceSurvivalCalculator.calculateWater(28.0, 0.0, 2)
        assertEquals(ResourceStatus.SUFFICIENT, est.status)
        assertEquals(7.0, est.estimatedDaysDrinking, 0.1)
    }

    @Test
    fun water_under2Days_returnsCritical() {
        // 2 people x 2L/day = 4L/day -> 6L = 1.5 days
        val est = ResourceSurvivalCalculator.calculateWater(6.0, 0.0, 2)
        assertEquals(ResourceStatus.CRITICAL, est.status)
    }

    @Test
    fun water_between2And5Days_returnsLimited() {
        // 2 people x 2L/day = 4L/day -> 14L = 3.5 days
        val est = ResourceSurvivalCalculator.calculateWater(14.0, 0.0, 2)
        assertEquals(ResourceStatus.LIMITED, est.status)
    }

    @Test
    fun water_totalLitersIncludesDrinkingAndUtility() {
        val est = ResourceSurvivalCalculator.calculateWater(5.0, 3.0, 1)
        assertEquals(8.0, est.totalLiters, 0.001)
    }

    @Test
    fun water_zeroPeople_doesNotCrash() {
        val est = ResourceSurvivalCalculator.calculateWater(10.0, 0.0, 0)
        assertTrue(est.totalPeople >= 1)
    }

    // -------------------------------------------------------------------------
    // Food
    // -------------------------------------------------------------------------

    @Test
    fun food_zeroMeals_returnsCritical() {
        val est = ResourceSurvivalCalculator.calculateFood(0, 2, 0, 0)
        assertEquals(ResourceStatus.CRITICAL, est.status)
    }

    @Test
    fun food_sufficientFor8Days_returnsSufficient() {
        // 2 people x 2 meals/day = 4 meals/day -> 32 meals = 8 days
        val est = ResourceSurvivalCalculator.calculateFood(32, 2, 0, 1)
        assertEquals(ResourceStatus.SUFFICIENT, est.status)
    }

    @Test
    fun food_under2Days_returnsCritical() {
        // 2 people x 2 meals/day = 4 meals/day -> 6 meals = 1.5 days
        val est = ResourceSurvivalCalculator.calculateFood(6, 2, 0, 1)
        assertEquals(ResourceStatus.CRITICAL, est.status)
    }

    @Test
    fun food_between2And6Days_returnsLimited() {
        // 2 people x 2 meals/day = 4 meals/day -> 16 meals = 4 days
        val est = ResourceSurvivalCalculator.calculateFood(16, 2, 0, 1)
        assertEquals(ResourceStatus.LIMITED, est.status)
    }

    @Test
    fun food_perishablePresent_recommendationMentionsPerishable() {
        val est = ResourceSurvivalCalculator.calculateFood(20, 2, 1, 1)
        assertTrue(est.recommendation.lowercase().contains("perish"))
    }

    // -------------------------------------------------------------------------
    // Power
    // -------------------------------------------------------------------------

    @Test
    fun power_zeroBatteryNoPowerBank_returnsCritical() {
        val est = ResourceSurvivalCalculator.calculatePower(0, false, 0, false)
        assertEquals(ResourceStatus.CRITICAL, est.status)
    }

    @Test
    fun power_fullBatteryWithBank_returnsAdequateOrLimited() {
        val est = ResourceSurvivalCalculator.calculatePower(100, true, 100, false)
        assertTrue(est.status == ResourceStatus.SUFFICIENT || est.status == ResourceStatus.LIMITED)
    }

    @Test
    fun power_batterySaverExtendsBeyondNormal() {
        val normal = ResourceSurvivalCalculator.calculatePower(50, false, 0, false)
        val saver = ResourceSurvivalCalculator.calculatePower(50, false, 0, true)
        assertTrue(saver.estimatedHoursEco >= normal.estimatedHoursNormal)
    }

    @Test
    fun power_criticalThresholdAt20Percent() {
        val at20 = ResourceSurvivalCalculator.calculatePower(20, false, 0, false)
        val at21 = ResourceSurvivalCalculator.calculatePower(21, false, 0, false)
        assertEquals(ResourceStatus.CRITICAL, at20.status)
        assertEquals(ResourceStatus.LIMITED, at21.status)
    }

    // -------------------------------------------------------------------------
    // Fuel
    // -------------------------------------------------------------------------

    @Test
    fun fuel_noVehicle_returnsZeroRangeAndCritical() {
        val est = ResourceSurvivalCalculator.calculateFuel(false, 0, 12.0)
        assertEquals(false, est.hasVehicle)
        assertEquals(0.0, est.estimatedRangeKm, 0.001)
        assertEquals(ResourceStatus.CRITICAL, est.status)
    }

    @Test
    fun fuel_fullTank_returnsSufficientAndLargeRange() {
        val est = ResourceSurvivalCalculator.calculateFuel(true, 100, 12.0)
        assertTrue(est.hasVehicle)
        assertTrue(est.estimatedRangeKm > 400.0)
        assertEquals(ResourceStatus.SUFFICIENT, est.status)
    }

    @Test
    fun fuel_lowPercent_returnsCritical() {
        val est = ResourceSurvivalCalculator.calculateFuel(true, 10, 12.0)
        assertEquals(ResourceStatus.CRITICAL, est.status)
    }

    @Test
    fun fuel_rangeProportionalToPercent() {
        val half = ResourceSurvivalCalculator.calculateFuel(true, 50, 12.0)
        val full = ResourceSurvivalCalculator.calculateFuel(true, 100, 12.0)
        assertEquals(full.estimatedRangeKm / 2.0, half.estimatedRangeKm, 1.0)
    }
}
 
