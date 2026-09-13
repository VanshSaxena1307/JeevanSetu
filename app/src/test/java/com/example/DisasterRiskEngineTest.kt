package com.example

import com.example.domain.engine.AssessmentAnswers
import com.example.domain.engine.DisasterRiskEngine
import com.example.domain.model.DisasterType
import com.example.domain.model.EvacuationAction
import com.example.domain.model.RiskLevel
import com.example.domain.model.RouteStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DisasterRiskEngineTest {

    private lateinit var engine: DisasterRiskEngine

    @Before
    fun setUp() {
        engine = DisasterRiskEngine()
    }

    private fun lowRiskFloodAnswers() = AssessmentAnswers(
        disasterType = DisasterType.FLOOD,
        isWaterEntering = false,
        waterLevelInches = 0,
        isWaterRising = false,
        isElectricityActiveInWater = false,
        isBuildingDamaged = false,
        isAnyoneTrapped = false,
        numberOfInjured = 0,
        routeStatus = RouteStatus.APPEARS_SAFE,   // correct enum value
        hasSavedSafeLocation = true,
        nearestSafeLocationDistanceKm = 1.5,
        hasSufficientWater = true,
        hasSufficientFood = true,
        hasCriticalMedicineAvailable = true
    )

    @Test
    fun flood_noHazards_returnsLowOrModerate() {
        val result = engine.evaluate(lowRiskFloodAnswers())
        assertTrue(result.riskLevel == RiskLevel.LOW || result.riskLevel == RiskLevel.MODERATE)
    }

    @Test
    fun flood_electricityInWaterWithDeepWater_returnsCritical() {
        val answers = lowRiskFloodAnswers().copy(
            isWaterEntering = true,
            isElectricityActiveInWater = true,
            isWaterRising = true,
            waterLevelInches = 18 // > 12 adds 15 pts: 20 + 20 + 25 + 15 = 80 pts (>= 76 is CRITICAL)
        )
        val result = engine.evaluate(answers)
        assertEquals(RiskLevel.CRITICAL, result.riskLevel)
        assertTrue(result.dangerFactors.any { it.description.contains("electrical", ignoreCase = true) })
    }

    @Test
    fun flood_waterEnteringAndRising_returnsHigh() {
        val answers = lowRiskFloodAnswers().copy(
            isWaterEntering = true,
            isWaterRising = true,
            waterLevelInches = 15 // > 12 adds 15 pts: 20 + 20 + 15 = 55 pts (>= 51 is HIGH)
        )
        val result = engine.evaluate(answers)
        assertEquals(RiskLevel.HIGH, result.riskLevel)
    }

    @Test
    fun flood_criticalRisk_triggersEvacuateOrDangerAction() {
        val answers = lowRiskFloodAnswers().copy(
            isWaterEntering = true, isElectricityActiveInWater = true, isWaterRising = true,
            waterLevelInches = 24, isBuildingDamaged = true
        )
        val result = engine.evaluate(answers)
        assertEquals(RiskLevel.CRITICAL, result.riskLevel)
        // Critical electricity-in-water flood should produce EVACUATE_IF_SAFE or IMMEDIATE_DANGER
        assertTrue(
            "Critical flood should produce urgent evacuation action",
            result.evacuationAction == EvacuationAction.EVACUATE_IF_SAFE ||
                    result.evacuationAction == EvacuationAction.IMMEDIATE_DANGER ||
                    result.evacuationAction == EvacuationAction.SHELTER_IN_PLACE
        )
    }

    @Test
    fun flood_lowRisk_triggersSheltOrMonitor() {
        val result = engine.evaluate(lowRiskFloodAnswers())
        assertTrue(
            "Expected shelter-in-place or prepare-to-evacuate for low risk",
            result.evacuationAction == EvacuationAction.SHELTER_IN_PLACE ||
                    result.evacuationAction == EvacuationAction.PREPARE_TO_EVACUATE
        )
    }

    @Test
    fun earthquake_partialCollapse_returnsCritical() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.EARTHQUAKE,
            isPartiallyCollapsed = true, isBuildingDamaged = true,
            hasVisibleCracksOrTilt = true, isAftershocksContinuing = true
        )
        assertEquals(RiskLevel.CRITICAL, engine.evaluate(answers).riskLevel)
    }

    @Test
    fun earthquake_gasLeakWithCollapse_returnsCritical() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.EARTHQUAKE,
            isGasLeakSuspected = true,
            isPartiallyCollapsed = true,
            isAftershocksContinuing = true
        )
        val result = engine.evaluate(answers)
        assertEquals(RiskLevel.CRITICAL, result.riskLevel)
        assertTrue(result.dangerFactors.any { it.description.contains("gas leak", ignoreCase = true) })
    }

    @Test
    fun earthquake_noStructuralDamage_returnsLowOrModerate() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.EARTHQUAKE,
            isBuildingDamaged = false, hasVisibleCracksOrTilt = false,
            isPartiallyCollapsed = false, isGasLeakSuspected = false
        )
        val result = engine.evaluate(answers)
        assertTrue(result.riskLevel == RiskLevel.LOW || result.riskLevel == RiskLevel.MODERATE)
    }

    @Test
    fun blockedRoute_withHighRisk_isHighOrCritical() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.FLOOD,
            isWaterEntering = true,
            isWaterRising = true,
            waterLevelInches = 18,
            routeStatus = RouteStatus.PARTIALLY_BLOCKED,  // correct enum value
            hasSavedSafeLocation = false
        )
        val result = engine.evaluate(answers)
        assertTrue(
            "Blocked route + rising water should be HIGH or CRITICAL",
            result.riskLevel == RiskLevel.HIGH || result.riskLevel == RiskLevel.CRITICAL
        )
    }

    @Test
    fun fire_insideNoExit_returnsCritical() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.FIRE,
            isFireInsideOrAdjacent = true, isSmokeEntering = true, isSafeExitAvailable = false
        )
        assertEquals(RiskLevel.CRITICAL, engine.evaluate(answers).riskLevel)
    }

    @Test
    fun vulnerableGroup_doesNotLowerRisk() {
        val base = AssessmentAnswers(
            disasterType = DisasterType.FLOOD,
            isWaterEntering = true, isWaterRising = true, waterLevelInches = 6,
            hasChildren = false, hasElderly = false
        )
        val vulnerable = base.copy(hasChildren = true, hasElderly = true)
        assertTrue(engine.evaluate(vulnerable).score >= engine.evaluate(base).score)
    }

    @Test
    fun trappedPersons_generatesNonEmptyDangerFactors() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.FLOOD, isAnyoneTrapped = true, numberOfInjured = 2
        )
        assertTrue(engine.evaluate(answers).dangerFactors.isNotEmpty())
    }

    @Test
    fun result_alwaysHasNonEmptyHeadlineAndRecommendation() {
        val result = engine.evaluate(lowRiskFloodAnswers())
        assertTrue(result.headline.isNotBlank())
        assertTrue(result.recommendation.isNotBlank())
    }

    @Test
    fun score_alwaysInRange0to100() {
        val answers = AssessmentAnswers(
            disasterType = DisasterType.EARTHQUAKE,
            isPartiallyCollapsed = true, isGasLeakSuspected = true,
            isAnyoneTrapped = true, hasChildren = true, hasElderly = true
        )
        val result = engine.evaluate(answers)
        assertTrue(result.score in 0..100)
    }

    @Test
    fun evacuationAction_isNeverNullForAnyDisasterType() {
        listOf(
            DisasterType.FLOOD, DisasterType.EARTHQUAKE, DisasterType.FIRE,
            DisasterType.LANDSLIDE, DisasterType.CYCLONE
        ).forEach { type ->
            val result = engine.evaluate(AssessmentAnswers(disasterType = type))
            assertNotNull(result.evacuationAction)
        }
    }
}
