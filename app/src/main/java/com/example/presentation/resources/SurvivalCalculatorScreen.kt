package com.example.presentation.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.ResourceStatus
import com.example.presentation.components.EmergencyTopBar
import com.example.presentation.components.ResourceStatusPill
import com.example.presentation.components.SafetyDisclaimerCard
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.RescueCyan
import com.example.ui.theme.SafetyGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange

@Composable
fun SurvivalCalculatorScreen(
    viewModel: JeevanSetuViewModel,
    onBack: () -> Unit,
    onNavigateToFamily: () -> Unit,
    onNavigateToResources: () -> Unit
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val waterEst by viewModel.waterEstimate.collectAsStateWithLifecycle()
    val foodEst by viewModel.foodEstimate.collectAsStateWithLifecycle()
    val powerEst by viewModel.powerEstimate.collectAsStateWithLifecycle()
    val fuelEst by viewModel.fuelEstimate.collectAsStateWithLifecycle()
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()

    val totalPeople = (userProfile?.numberOfAdults ?: 2) +
            (userProfile?.numberOfChildren ?: 0) +
            (userProfile?.numberOfElderly ?: 0)

    val criticalMedsWarning = medicines.filter { it.isCritical && it.daysRemaining <= 4 }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            EmergencyTopBar(
                title = "SURVIVAL FORECAST & ADVISOR",
                isOnline = isOnline,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(innerPadding)
                .testTag("survival_calculator_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ESTIMATED CONSUMPTION BASIS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            )
                            Text(
                                text = "$totalPeople Family Members",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Adults: ${userProfile?.numberOfAdults ?: 2} • Children: ${userProfile?.numberOfChildren ?: 0} • Elderly: ${userProfile?.numberOfElderly ?: 0}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onNavigateToFamily,
                            colors = ButtonDefaults.buttonColors(containerColor = MintLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("EDIT FAMILY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintDeep)
                        }
                    }
                }
            }

            // Summary Forecast Cards
            item {
                Text(
                    text = "ESTIMATED SURVIVAL RESERVES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                )
            }

            item {
                SurvivalItemCard(
                    title = "WATER RESERVES",
                    value = "${String.format("%.1f", waterEst.estimatedDaysDrinking)} Days",
                    status = waterEst.status,
                    details = "Total: ${waterEst.totalLiters}L (Drinking: ${waterEst.drinkingLiters}L, Utility: ${waterEst.utilityLiters}L) at standard 3.0L/person/day.",
                    icon = Icons.Default.WaterDrop,
                    accentColor = RescueCyan
                )
            }

            item {
                SurvivalItemCard(
                    title = "FOOD RESERVES",
                    value = "${String.format("%.1f", foodEst.estimatedDaysRemaining)} Days",
                    status = foodEst.status,
                    details = "Total: ${foodEst.totalMeals} meals available for $totalPeople individuals (${foodEst.perishableCount} perishable, ${foodEst.nonPerishableCount} shelf-stable).",
                    icon = Icons.Default.Restaurant,
                    accentColor = SafetyGreen
                )
            }

            item {
                SurvivalItemCard(
                    title = "PHONE & BACKUP POWER",
                    value = "~${powerEst.estimatedHoursEco} Hours",
                    status = powerEst.status,
                    details = "Phone Battery: ${powerEst.phoneBatteryPercent}% • Backup Power Bank: ${if (powerEst.powerBankAvailable) "Available" else "None"}. Conservation extends runtime significantly.",
                    icon = Icons.Default.BatteryAlert,
                    accentColor = CautionAmber
                )
            }

            item {
                SurvivalItemCard(
                    title = "EVACUATION VEHICLE FUEL",
                    value = "~${String.format("%.0f", fuelEst.estimatedRangeKm)} km",
                    status = fuelEst.status,
                    details = "Fuel Tank: ${fuelEst.fuelPercent}%. Sufficient for direct transit to designated safe shelters.",
                    icon = Icons.Default.LocalGasStation,
                    accentColor = WarningOrange
                )
            }

            if (criticalMedsWarning.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4F4)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD5D5))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Medication, contentDescription = null, tint = EmergencyRed)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "CRITICAL MEDICATION EXPIRY WARNING",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmergencyRed
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            criticalMedsWarning.forEach { med ->
                                Text(
                                    text = "• ${med.name}: Only ${med.daysRemaining} days of doses left! Prioritize resupply at nearest emergency medical camp.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF4A1A1A))
                                )
                            }
                        }
                    }
                }
            }

            // Dynamic Conservation Advisor
            item {
                Text(
                    text = "DYNAMIC RESOURCE CONSERVATION ADVISOR",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                )
            }

            item {
                ConservationAdviceCard(
                    category = "WATER CONSERVATION RULES",
                    tips = listOf(
                        "Ration drinking: In critical scarcity, adults need minimum 1.5 - 2.0 liters/day. Never ration to zero.",
                        "Avoid thirst-inducing foods: Minimize highly salted, sugary, or dry packaged crackers.",
                        "Recycle utility water: Use wash/sponge runoff for toilet flushing or dampening cooling cloths.",
                        "Purify before ingestion: Boil for 1 full minute or add 2 drops chlorine bleach per liter."
                    ),
                    accentColor = MintDeep
                )
            }

            item {
                ConservationAdviceCard(
                    category = "FOOD RATIONING & SPOILAGE PROTOCOL",
                    tips = listOf(
                        "Priority 1 - Consume perishables: Eat open dairy, cooked meals, and fresh produce within 24-48 hours before power loss ruins them.",
                        "Keep cooler/refrigerator doors sealed: An unopened refrigerator preserves cold for ~4 hours; a full freezer maintains chill for 48 hours.",
                        "Preserve dry staples: Keep rice, pulses, and canned goods sealed in watertight containers away from flood dampness."
                    ),
                    accentColor = SafetyGreen
                )
            }

            item {
                ConservationAdviceCard(
                    category = "BATTERY & POWER CONSERVATION",
                    tips = listOf(
                        "Activate Ultra Battery Saver: Disable Bluetooth, GPS auto-scanning, background sync, and haptics.",
                        "Lower screen luminance: Set brightness to 20% or lowest comfortable reading level.",
                        "Scheduled communication windows: Turn phone to Airplane Mode; power on for 5 minutes at the top of every 2 hours to send/receive emergency SMS."
                    ),
                    accentColor = CautionAmber
                )
            }

            item {
                Button(
                    onClick = onNavigateToResources,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                ) {
                    Text("UPDATE INVENTORY QUANTITIES", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            item {
                SafetyDisclaimerCard()
            }
        }
    }
}

@Composable
fun SurvivalItemCard(
    title: String,
    value: String,
    status: ResourceStatus,
    details: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                }
                ResourceStatusPill(status = status, label = "")
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(status.hexColor)
                )
            )
            Text(
                text = details,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@Composable
fun ConservationAdviceCard(
    category: String,
    tips: List<String>,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                )
            }
            tips.forEach { tip ->
                Text(
                    text = "• $tip",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
