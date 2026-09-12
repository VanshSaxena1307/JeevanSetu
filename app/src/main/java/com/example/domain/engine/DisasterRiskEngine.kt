package com.example.domain.engine

import com.example.domain.model.DangerFactor
import com.example.domain.model.DisasterType
import com.example.domain.model.EvacuationAction
import com.example.domain.model.RiskLevel
import com.example.domain.model.RiskResult
import com.example.domain.model.RouteStatus

data class AssessmentAnswers(
    val disasterType: DisasterType,
    // Flood specifics
    val isWaterEntering: Boolean = false,
    val waterLevelInches: Int = 0,
    val isWaterRising: Boolean = false,
    val isElectricityActiveInWater: Boolean = false,
    // Structural / General specifics
    val isBuildingDamaged: Boolean = false,
    val hasVisibleCracksOrTilt: Boolean = false,
    val isPartiallyCollapsed: Boolean = false,
    val isAftershocksContinuing: Boolean = false,
    val isGasLeakSuspected: Boolean = false,
    // Fire / Smoke specifics
    val isFireInsideOrAdjacent: Boolean = false,
    val isSmokeEntering: Boolean = false,
    val isSafeExitAvailable: Boolean = true,
    // Landslide specifics
    val isNearUnstableSlope: Boolean = false,
    val isSoilOrDebrisMoving: Boolean = false,
    val isHeavyRainContinuing: Boolean = false,
    // Trapped / Injury
    val isAnyoneTrapped: Boolean = false,
    val numberOfInjured: Int = 0,
    // People
    val hasChildren: Boolean = false,
    val hasElderly: Boolean = false,
    val hasSpecialAssistanceNeeds: Boolean = false,
    // Route & Safe Location
    val routeStatus: RouteStatus = RouteStatus.UNKNOWN,
    val hasSavedSafeLocation: Boolean = false,
    val nearestSafeLocationDistanceKm: Double? = null,
    // Supplies
    val hasSufficientWater: Boolean = true,
    val hasSufficientFood: Boolean = true,
    val hasCriticalMedicineAvailable: Boolean = true
)

class DisasterRiskEngine {

    fun evaluate(answers: AssessmentAnswers): RiskResult {
        var score = 0
        val factors = mutableListOf<DangerFactor>()
        val resourceWarnings = mutableListOf<String>()
        val evacuationCautions = mutableListOf<String>()

        // 1. Immediate Environmental Threat
        var envScore = 0
        when (answers.disasterType) {
            DisasterType.FLOOD, DisasterType.FLASH_FLOOD -> {
                if (answers.isWaterEntering) {
                    envScore += 20
                    factors.add(DangerFactor(RiskLevel.HIGH, "Floodwater is actively entering the structure", "Environment"))
                }
                if (answers.isWaterRising) {
                    envScore += 20
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Water levels are actively rising", "Environment"))
                }
                if (answers.waterLevelInches > 12) {
                    envScore += 15
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Water depth exceeds safe pedestrian threshold (> 1 foot)", "Environment"))
                }
                if (answers.isElectricityActiveInWater) {
                    envScore += 25
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Active electrical hazard in submerged area", "Immediate Threat"))
                }
            }
            DisasterType.FIRE -> {
                if (answers.isFireInsideOrAdjacent) {
                    envScore += 45
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Active fire present inside or immediately adjacent to building", "Immediate Threat"))
                }
                if (answers.isSmokeEntering) {
                    envScore += 25
                    factors.add(DangerFactor(RiskLevel.HIGH, "Toxic smoke or fumes infiltrating shelter", "Immediate Threat"))
                }
                if (!answers.isSafeExitAvailable) {
                    envScore += 30
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Primary exits compromised or obstructed", "Route Hazard"))
                }
            }
            DisasterType.EARTHQUAKE, DisasterType.BUILDING_COLLAPSE -> {
                if (answers.isPartiallyCollapsed) {
                    envScore += 45
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Partial structural collapse detected", "Structural Danger"))
                }
                if (answers.hasVisibleCracksOrTilt) {
                    envScore += 25
                    factors.add(DangerFactor(RiskLevel.HIGH, "Severe structural fissures or foundation tilt reported", "Structural Danger"))
                }
                if (answers.isGasLeakSuspected) {
                    envScore += 25
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Potential gas leak or flammable vapor detected", "Immediate Threat"))
                }
                if (answers.isAftershocksContinuing) {
                    envScore += 15
                    factors.add(DangerFactor(RiskLevel.MODERATE, "Continuing seismic aftershocks destabilizing building", "Environment"))
                }
            }
            DisasterType.LANDSLIDE -> {
                if (answers.isSoilOrDebrisMoving) {
                    envScore += 40
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Active ground, mud, or rock mass movement underway", "Immediate Threat"))
                }
                if (answers.isNearUnstableSlope) {
                    envScore += 20
                    factors.add(DangerFactor(RiskLevel.HIGH, "Location situated directly in steep or unstable slope zone", "Environment"))
                }
                if (answers.isHeavyRainContinuing) {
                    envScore += 15
                    factors.add(DangerFactor(RiskLevel.MODERATE, "Prolonged heavy precipitation increasing slide risk", "Environment"))
                }
            }
            DisasterType.CYCLONE -> {
                if (answers.isBuildingDamaged) {
                    envScore += 25
                    factors.add(DangerFactor(RiskLevel.HIGH, "Roof, doors, or windows breached by high winds", "Structural Danger"))
                }
                if (answers.isWaterEntering) {
                    envScore += 25
                    factors.add(DangerFactor(RiskLevel.CRITICAL, "Coastal storm surge or torrential inundation entering", "Environment"))
                }
            }
            else -> {
                if (answers.isBuildingDamaged) {
                    envScore += 20
                    factors.add(DangerFactor(RiskLevel.HIGH, "Building structure integrity compromised", "Structural Danger"))
                }
            }
        }
        score += envScore

        // 2. Immediate Life Threat / Trapped
        if (answers.isAnyoneTrapped) {
            score += 35
            factors.add(DangerFactor(RiskLevel.CRITICAL, "Individuals are trapped and unable to self-evacuate", "Life Threat"))
            evacuationCautions.add("Focus on signaling for rescue; do not attempt unsafe solo vertical climbs.")
        }
        if (answers.numberOfInjured > 0) {
            score += 15 + (answers.numberOfInjured.coerceAtMost(3) * 5)
            factors.add(DangerFactor(RiskLevel.HIGH, "${answers.numberOfInjured} injured individual(s) requiring medical assistance", "Vulnerability"))
        }

        // 3. Vulnerable Dependents
        if (answers.hasChildren) {
            score += 8
            factors.add(DangerFactor(RiskLevel.MODERATE, "Children present needing supervision and safe conveyance", "Vulnerability"))
        }
        if (answers.hasElderly) {
            score += 10
            factors.add(DangerFactor(RiskLevel.MODERATE, "Elderly present with potential reduced mobility", "Vulnerability"))
        }
        if (answers.hasSpecialAssistanceNeeds) {
            score += 12
            factors.add(DangerFactor(RiskLevel.HIGH, "Special medical or mobility equipment required", "Vulnerability"))
        }

        // 4. Resource Vulnerabilities
        if (!answers.hasSufficientWater) {
            score += 12
            factors.add(DangerFactor(RiskLevel.HIGH, "Drinking water reserves severely low or depleted", "Resources"))
            resourceWarnings.add("Drinking water critical: Strictly ration remaining potable fluid; avoid salted foods.")
        }
        if (!answers.hasSufficientFood) {
            score += 8
            resourceWarnings.add("Food reserves limited: Prioritize perishable goods before they spoil.")
        }
        if (!answers.hasCriticalMedicineAvailable) {
            score += 15
            factors.add(DangerFactor(RiskLevel.HIGH, "Vital life-sustaining medicines exhausted or near depletion", "Resources"))
            resourceWarnings.add("Critical medicine alert: Evacuation or emergency relief contact is urgent.")
        }

        // Cap raw score between 0 and 100
        val finalScore = score.coerceIn(0, 100)

        val riskLevel = when {
            finalScore >= 76 -> RiskLevel.CRITICAL
            finalScore >= 51 -> RiskLevel.HIGH
            finalScore >= 26 -> RiskLevel.MODERATE
            else -> RiskLevel.LOW
        }

        // 5. Evacuation vs Shelter-in-Place Analysis
        // CRITICAL: We evaluate the route condition so we NEVER tell users to step blindly into dangerous floods or fires.
        val evacuationAction: EvacuationAction
        val headline: String
        val recommendation: String

        when {
            // Case 1: Active fire inside or severe collapse threat
            (answers.disasterType == DisasterType.FIRE && answers.isFireInsideOrAdjacent) ||
            answers.isPartiallyCollapsed ||
            answers.isSoilOrDebrisMoving -> {
                if (answers.routeStatus == RouteStatus.DANGEROUS && !answers.isSafeExitAvailable) {
                    evacuationAction = EvacuationAction.IMMEDIATE_DANGER
                    headline = "CRITICAL: Immediate Danger — Primary Exits Obstructed"
                    recommendation = "You are in an immediate life-threatening situation. Primary exits are compromised. Seek the safest alternate boundary, stay close to the floor below smoke, signal emergency rescue (flashlights, loud whistles, bright fabric), and prepare to evacuate through windows or emergency apertures only if safer than remaining."
                } else {
                    evacuationAction = EvacuationAction.EVACUATE_IF_SAFE
                    headline = "CRITICAL: Evacuate Structure Immediately Via Clear Path"
                    recommendation = "The current building presents extreme life danger. Evacuate immediately via your verified safe exit. Do not linger for possessions. Move perpendicular to slope movement or downwind of fire."
                }
            }

            // Case 2: Water rising rapidly or severe flood
            (answers.disasterType == DisasterType.FLOOD || answers.disasterType == DisasterType.FLASH_FLOOD) &&
            (answers.isWaterRising || answers.waterLevelInches > 12) -> {
                if (answers.routeStatus == RouteStatus.FLOODED || answers.routeStatus == RouteStatus.DANGEROUS) {
                    evacuationAction = EvacuationAction.SHELTER_IN_PLACE
                    headline = "HIGH RISK: Shelter at Highest Stable Level — Do Not Enter Floodwater"
                    recommendation = "Outside routes are flooded or hazardous. Moving into fast-flowing water is one of the highest causes of disaster fatalities (just 6 inches of moving water can knock down an adult). Move vertically to the highest stable floor or roof. Signal for emergency rescue. Do NOT attempt to traverse submerged roads."
                    evacuationCautions.add("Never drive or walk into moving floodwater ('Turn Around, Don't Drown').")
                } else {
                    evacuationAction = EvacuationAction.EVACUATE_IF_SAFE
                    headline = "HIGH RISK: Evacuate to Higher Ground While Route Remains Passable"
                    recommendation = "Water levels are actively rising. A safe path has been confirmed. Evacuate calmly to higher elevation immediately before ground corridors become inundated."
                }
            }

            // Case 3: High or Critical Risk generally
            finalScore >= 51 -> {
                if (answers.routeStatus == RouteStatus.DANGEROUS || answers.routeStatus == RouteStatus.FLOODED) {
                    evacuationAction = EvacuationAction.SHELTER_IN_PLACE
                    headline = "ELEVATED RISK: Danger on Outside Routes — Secure Safe In-Place Position"
                    recommendation = "Current conditions indicate significant danger, but external evacuation routes are currently hazardous. Remaining inside a reinforced part of your shelter is currently safer than exposing yourself to open hazards. Continue monitoring and signal if conditions deteriorate."
                } else {
                    evacuationAction = EvacuationAction.EVACUATE_IF_SAFE
                    headline = "HIGH RISK: Evacuation Advised via Safe Verified Route"
                    recommendation = "Risk factors are elevated. Move toward your nearest designated safe location while routes are clear. Carry your essential evacuation backpack, water, and identification documents."
                }
            }

            // Case 4: Moderate Risk
            finalScore >= 26 -> {
                evacuationAction = EvacuationAction.PREPARE_TO_EVACUATE
                headline = "MODERATE RISK: Prepare Essential Supplies & Monitor Routes"
                recommendation = "Conditions are destabilizing. Pack your go-bag, charge communication devices, fill all clean containers with drinking water, and verify the status of the nearest safe shelter. Be ready to move on short notice."
            }

            // Case 5: Low Risk
            else -> {
                evacuationAction = EvacuationAction.SHELTER_IN_PLACE
                headline = "LOW RISK: Shelter in Place, Conserve Resources & Stay Vigilant"
                recommendation = "No immediate life-threatening structural or environmental conditions are currently detected. Stay indoors, conserve water, food, and battery power, and continue monitoring emergency weather bulletins."
            }
        }

        val safeLocationNote = if (answers.hasSavedSafeLocation && answers.nearestSafeLocationDistanceKm != null) {
            "Nearest saved safe shelter is approximately ${String.format("%.1f", answers.nearestSafeLocationDistanceKm)} km away (straight-line). Confirm route safety before traveling."
        } else {
            "No safe location currently stored. Open 'Safe Locations' to save designated community shelters."
        }

        return RiskResult(
            score = finalScore,
            riskLevel = riskLevel,
            evacuationAction = evacuationAction,
            headline = headline,
            recommendation = recommendation,
            dangerFactors = factors,
            resourceWarnings = resourceWarnings,
            evacuationCautions = evacuationCautions,
            safeLocationNote = safeLocationNote
        )
    }
}
