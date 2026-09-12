package com.example.domain.model

data class DisasterGuide(
    val id: String,
    val type: DisasterType,
    val title: String,
    val summary: String,
    val beforeSteps: List<String>,
    val duringSteps: List<String>,
    val afterSteps: List<String>,
    val doList: List<String>,
    val dontList: List<String>,
    val highRiskThreshold: String
)

data class FirstAidTopic(
    val id: String,
    val title: String,
    val category: String,
    val urgencyLevel: String, // URGENT, HIGH, MODERATE
    val summary: String,
    val immediateActions: List<String>,
    val criticalDonts: List<String>,
    val whenToSeekEvacuation: String
)
