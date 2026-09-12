package com.example.domain.model

data class DisasterAlert(
    val id: String,
    val title: String,
    val issuedBy: String,
    val location: String,
    val timestamp: String,
    val severity: AlertSeverity,
    val cardType: AlertCardType,
    val description: String,
    val dos: List<String>,
    val donts: List<String>
)

enum class AlertSeverity(val label: String) {
    LOW("Low Intensity"),
    MODERATE("Moderate Intensity"),
    HIGH("High Intensity"),
    SEVERE("Severe Intensity")
}

enum class AlertCardType {
    WHITE_CARD,
    PEACH_CARD,
    CORAL_CARD
}
