package com.example.domain.model

enum class DisasterType(val displayName: String, val iconResName: String, val description: String) {
    FLOOD("Flood", "ic_flood", "River or seasonal flooding with rising water levels"),
    FLASH_FLOOD("Flash Flood", "ic_flash_flood", "Sudden, rapid rush of high-velocity water"),
    EARTHQUAKE("Earthquake", "ic_earthquake", "Ground shaking, structural tremors, and aftershocks"),
    LANDSLIDE("Landslide", "ic_landslide", "Downslope movement of rock, soil, or debris"),
    CYCLONE("Cyclone / Storm", "ic_cyclone", "Destructive winds, torrential rain, and storm surges"),
    FIRE("Fire / Wildfire", "ic_fire", "Building or wildfire spreading with heat and smoke"),
    HEATWAVE("Extreme Heatwave", "ic_heatwave", "Life-threatening ambient temperatures and dehydration"),
    EXTREME_COLD("Extreme Cold", "ic_cold", "Severe freezing temperatures and blizzard risk"),
    BUILDING_COLLAPSE("Building Collapse", "ic_collapse", "Structural failure or compromised foundations"),
    OTHER("Other Emergency", "ic_alert", "General disaster or unforeseen emergency")
}

enum class RiskLevel(val label: String, val hexColor: Long, val badgeText: String) {
    LOW("LOW RISK", 0xFF10B981, "🟢 LOW RISK"),
    MODERATE("MODERATE RISK", 0xFFF59E0B, "🟡 MODERATE RISK"),
    HIGH("HIGH RISK", 0xFFF97316, "🟠 HIGH RISK"),
    CRITICAL("CRITICAL RISK", 0xFFEF4444, "🔴 CRITICAL RISK")
}

enum class EvacuationAction(val title: String, val badgeText: String, val hexColor: Long) {
    SHELTER_IN_PLACE("SHELTER IN PLACE", "🟢 SHELTER IN PLACE", 0xFF10B981),
    PREPARE_TO_EVACUATE("PREPARE TO EVACUATE", "🟡 PREPARE TO EVACUATE", 0xFFF59E0B),
    EVACUATE_IF_SAFE("EVACUATE IF SAFE", "🟠 EVACUATE IF SAFE", 0xFFF97316),
    IMMEDIATE_DANGER("IMMEDIATE DANGER", "🔴 IMMEDIATE DANGER", 0xFFEF4444)
}

enum class RouteStatus(val label: String, val penaltyScore: Int) {
    APPEARS_SAFE("Route appears safe", 0),
    PARTIALLY_BLOCKED("Route partially blocked / debris", 15),
    FLOODED("Route flooded or submerged", 30),
    DANGEROUS("Route visibly hazardous / active threat", 40),
    UNKNOWN("Route status unknown / unconfirmed", 20)
}

data class DangerFactor(
    val severity: RiskLevel,
    val description: String,
    val category: String
)

data class RiskResult(
    val score: Int, // 0 to 100
    val riskLevel: RiskLevel,
    val evacuationAction: EvacuationAction,
    val headline: String,
    val recommendation: String,
    val dangerFactors: List<DangerFactor>,
    val resourceWarnings: List<String>,
    val evacuationCautions: List<String>,
    val safeLocationNote: String?,
    val disclaimer: String = "This is a decision-support recommendation based on the information available in the app. Always follow official emergency authorities and evacuation orders when available."
)

data class WaterSurvivalEstimate(
    val totalLiters: Double,
    val drinkingLiters: Double,
    val utilityLiters: Double,
    val totalPeople: Int,
    val estimatedDaysDrinking: Double,
    val status: ResourceStatus,
    val recommendation: String
)

data class FoodSurvivalEstimate(
    val totalMeals: Int,
    val totalPeople: Int,
    val estimatedDaysRemaining: Double,
    val perishableCount: Int,
    val nonPerishableCount: Int,
    val status: ResourceStatus,
    val recommendation: String
)

data class MedicineSurvivalEstimate(
    val totalItems: Int,
    val criticalShortages: List<String>,
    val minDaysRemaining: Int,
    val status: ResourceStatus
)

data class PowerSurvivalEstimate(
    val phoneBatteryPercent: Int,
    val powerBankAvailable: Boolean,
    val estimatedHoursNormal: Int,
    val estimatedHoursEco: Int,
    val status: ResourceStatus,
    val conservationTips: List<String>
)

data class FuelSurvivalEstimate(
    val hasVehicle: Boolean,
    val fuelPercent: Int,
    val estimatedRangeKm: Double,
    val canReachSafeLocation: Boolean,
    val status: ResourceStatus
)

enum class ResourceStatus(val label: String, val hexColor: Long) {
    SUFFICIENT("Sufficient", 0xFF10B981),
    LIMITED("Limited", 0xFFF59E0B),
    CRITICAL("Critical", 0xFFEF4444)
}
