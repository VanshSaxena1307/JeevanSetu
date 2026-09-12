package com.example.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.domain.model.AppLanguage
import com.example.domain.model.ResourceStatus
import com.example.domain.model.RiskLevel
import com.example.presentation.components.CircularResourceGauge
import com.example.presentation.components.JeevanSetuLogo
import com.example.presentation.viewmodel.JeevanSetuViewModel
import java.util.Locale
import com.example.ui.theme.JeevanBatteryAmber
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanEquipmentCyan
import com.example.ui.theme.JeevanFoodYellow
import com.example.ui.theme.JeevanFuelOrange
import com.example.ui.theme.JeevanGreenBg
import com.example.ui.theme.JeevanGreenBorder
import com.example.ui.theme.JeevanMedicalRed
import com.example.ui.theme.JeevanTextMuted
import com.example.ui.theme.JeevanWaterBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: JeevanSetuViewModel,
    onNavigateToAssessment: () -> Unit,
    onNavigateToEvacuation: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToResources: () -> Unit,
    onNavigateToCalculator: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToFirstAid: () -> Unit,
    onNavigateToSafeLocations: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToFamily: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val displayLocation by viewModel.displayLocation.collectAsStateWithLifecycle()
    val activeAlerts by viewModel.activeAlerts.collectAsStateWithLifecycle()
    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val waterEstimate by viewModel.waterEstimate.collectAsStateWithLifecycle()
    val foodEstimate by viewModel.foodEstimate.collectAsStateWithLifecycle()
    val powerEstimate by viewModel.powerEstimate.collectAsStateWithLifecycle()
    val fuelEstimate by viewModel.fuelEstimate.collectAsStateWithLifecycle()
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()
    val checklistItems by viewModel.checklistItems.collectAsStateWithLifecycle()

    var showLanguageSheet by remember { mutableStateOf(false) }

    val isOnline = dashboardState.isOnline
    val networkStatusText = if (isOnline) "Connected" else "Offline Mode"
    val networkStatusColor = if (isOnline) Color(0xFF38BDF8) else JeevanBrandGreen

    val totalPeople = remember(userProfile) {
        val adults = userProfile?.numberOfAdults ?: 0
        val children = userProfile?.numberOfChildren ?: 0
        val elderly = userProfile?.numberOfElderly ?: 0
        val injured = userProfile?.numberOfInjured ?: 0
        adults + children + elderly + injured
    }
    val peopleDisplay = if (totalPeople > 0) "$totalPeople people" else "Not set"

    val locationDisplay = remember(displayLocation, dashboardState.userLocation, dashboardState.isGpsActive, dashboardState.isOnline, userProfile) {
        val name = when {
            displayLocation.city.isNotBlank() && displayLocation.city != "Bengaluru" -> displayLocation.city
            !userProfile?.regionName.isNullOrBlank() -> userProfile!!.regionName
            displayLocation.city.isNotBlank() -> displayLocation.city
            dashboardState.userLocation.latitude != 0.0 && dashboardState.userLocation.longitude != 0.0 ->
                String.format(Locale.US, "%.2f, %.2f", dashboardState.userLocation.latitude, dashboardState.userLocation.longitude)
            else -> ""
        }

        if (name.isBlank()) {
            "Location unavailable"
        } else {
            val status = when {
                dashboardState.isGpsActive -> "GPS Active"
                dashboardState.isOnline -> "Network"
                else -> "Offline"
            }
            "$name\n( $status )"
        }
    }

    val timeSinceDisplay = remember(dashboardState.latestAssessment) {
        val assessment = dashboardState.latestAssessment
        if (assessment != null && assessment.timestamp > 0L) {
            val elapsedMs = (System.currentTimeMillis() - assessment.timestamp).coerceAtLeast(0L)
            val mins = (elapsedMs / 60000L).toInt()
            val hours = (elapsedMs / 3600000L).toInt()
            val days = (elapsedMs / 86400000L).toInt()
            when {
                days > 0 -> "$days day${if (days > 1) "s" else ""} ${hours % 24}h"
                hours > 0 -> "$hours hr${if (hours > 1) "s" else ""} ${mins % 60}m"
                else -> "$mins min${if (mins != 1) "s" else ""}"
            }
        } else {
            "Not assessed"
        }
    }

    val nextCheckInDisplay = remember(dashboardState.latestAssessment, dashboardState.overallRiskLevel) {
        val assessment = dashboardState.latestAssessment
        if (assessment != null && assessment.timestamp > 0L) {
            val intervalHours = if (dashboardState.overallRiskLevel == RiskLevel.CRITICAL) 4 else 6
            val intervalMs = intervalHours * 3600000L
            val elapsedMs = (System.currentTimeMillis() - assessment.timestamp).coerceAtLeast(0L)
            val elapsedInInterval = elapsedMs % intervalMs
            val remainingMs = intervalMs - elapsedInInterval
            val remHours = (remainingMs / 3600000L).toInt()
            val remMins = ((remainingMs % 3600000L) / 60000L).toInt()
            if (remHours > 0) "in $remHours hr ${remMins}m" else "in $remMins min"
        } else {
            "Tap to assess"
        }
    }

    val bannerState = remember(
        waterEstimate,
        foodEstimate,
        powerEstimate,
        fuelEstimate,
        medicines,
        dashboardState.overallRiskLevel,
        dashboardState.latestAssessment,
        userProfile
    ) {
        val risk = dashboardState.overallRiskLevel
        val assessment = dashboardState.latestAssessment

        when {
            // CRITICAL: Zero or depleted drinking water
            waterEstimate.drinkingLiters <= 0.0 -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: NO WATER",
                    subtitle = "Zero drinking water available! Seek immediate potable hydration source or rescue."
                )
            }
            // CRITICAL: Water reserves < 2 days
            waterEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: WATER DEPLETION",
                    subtitle = "Water reserves will exhaust in ${String.format(Locale.US, "%.1f", waterEstimate.estimatedDaysDrinking)} days. Adopt emergency 1.5L/day rationing immediately."
                )
            }
            // CRITICAL: Extreme disaster danger / evacuation order
            risk == RiskLevel.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: IMMEDIATE DANGER",
                    subtitle = assessment?.headline ?: "Extreme hazard level active. Follow urgent evacuation directives."
                )
            }
            // CRITICAL: Zero food
            foodEstimate.totalMeals <= 0 -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: NO FOOD",
                    subtitle = "Zero food rations remaining. Adopt emergency hunger protocols and signal rescue."
                )
            }
            // CRITICAL: Food reserves < 2 days
            foodEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: FOOD SHORTAGE",
                    subtitle = "Food reserves critically low (${String.format(Locale.US, "%.1f", foodEstimate.estimatedDaysRemaining)} days). Limit to emergency survival rations."
                )
            }
            // CRITICAL: Battery <= 20%
            powerEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: BATTERY EXHAUSTION",
                    subtitle = "Device battery at ${powerEstimate.phoneBatteryPercent}%. Enable ultra battery saver immediately."
                )
            }
            // CRITICAL: Fuel critically low (<= 15%) when vehicle owned
            fuelEstimate.hasVehicle && fuelEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: FUEL DEPLETION",
                    subtitle = "Vehicle fuel at ${fuelEstimate.fuelPercent}%. Evacuation range (${fuelEstimate.estimatedRangeKm.toInt()} km) is below safe emergency threshold."
                )
            }
            // CRITICAL: Critical medicine running out (< 2 days)
            medicines.any { it.isCritical && (if (it.dailyUsage > 0) it.quantity / it.dailyUsage else it.daysRemaining) < 2 } -> {
                val med = medicines.first { it.isCritical && (if (it.dailyUsage > 0) it.quantity / it.dailyUsage else it.daysRemaining) < 2 }
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "CRITICAL: MEDICINE SHORTAGE",
                    subtitle = "Critical supply of ${med.name} exhausts in less than 48 hours."
                )
            }

            // WARNING: High disaster risk
            risk == RiskLevel.HIGH -> {
                BannerState(
                    severity = BannerSeverity.WARNING,
                    title = "WARNING: HIGH DISASTER RISK",
                    subtitle = assessment?.headline ?: "High threat conditions detected. Prepare evacuation bag and monitor local routes."
                )
            }
            // WARNING: Insufficient fuel to reach safe shelter
            fuelEstimate.hasVehicle && !fuelEstimate.canReachSafeLocation -> {
                BannerState(
                    severity = BannerSeverity.WARNING,
                    title = "WARNING: INSUFFICIENT FUEL",
                    subtitle = "Vehicle range (${fuelEstimate.estimatedRangeKm.toInt()} km) may be insufficient to reach safe shelter with detour buffers."
                )
            }

            // CONSERVE: Water limited (2 to 5 days)
            waterEstimate.status == ResourceStatus.LIMITED -> {
                val hours = (waterEstimate.estimatedDaysDrinking * 24).toInt()
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "CONSERVE WATER",
                    subtitle = "At current usage, water becomes critical in ${String.format(Locale.US, "%.1f", waterEstimate.estimatedDaysDrinking)} days ($hours hours). Restrict non-drinking use."
                )
            }
            // CONSERVE: Food limited (2 to 6 days)
            foodEstimate.status == ResourceStatus.LIMITED -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "CONSERVE FOOD",
                    subtitle = "Food supplies limited to ${String.format(Locale.US, "%.1f", foodEstimate.estimatedDaysRemaining)} days. Prioritize consuming perishable items first."
                )
            }
            // CONSERVE: Battery limited (<= 45%)
            powerEstimate.status == ResourceStatus.LIMITED -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "CONSERVE POWER",
                    subtitle = "Battery at ${powerEstimate.phoneBatteryPercent}%. Restrict screen time and disable background radios."
                )
            }
            // CONSERVE: Fuel limited (15% to 35%)
            fuelEstimate.hasVehicle && fuelEstimate.status == ResourceStatus.LIMITED -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "CONSERVE FUEL",
                    subtitle = "Fuel at ${fuelEstimate.fuelPercent}%. Reserve vehicle strictly for essential evacuation movement."
                )
            }
            // CONSERVE: Moderate disaster risk
            risk == RiskLevel.MODERATE -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "MONITOR: MODERATE RISK",
                    subtitle = assessment?.headline ?: "Moderate hazards present in area. Review emergency supplies and shelters."
                )
            }
            // CONSERVE: Unconfigured household profile
            userProfile == null -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "SETUP PROFILE",
                    subtitle = "Configure household members in Family Profile to calculate precise survival quotas."
                )
            }

            // SAFE: All resources sufficient and disaster risk low
            else -> {
                val minDays = minOf(waterEstimate.estimatedDaysDrinking, foodEstimate.estimatedDaysRemaining)
                BannerState(
                    severity = BannerSeverity.SAFE,
                    title = "SAFE",
                    subtitle = "Current supplies estimated to last ${String.format(Locale.US, "%.1f", minDays)} days."
                )
            }
        }
    }

    val bannerBg = when (bannerState.severity) {
        BannerSeverity.SAFE -> JeevanGreenBg
        BannerSeverity.CONSERVE -> Color(0xFF28200F)
        BannerSeverity.WARNING -> Color(0xFF301A0E)
        BannerSeverity.CRITICAL -> Color(0xFF331114)
    }
    val bannerBorder = when (bannerState.severity) {
        BannerSeverity.SAFE -> JeevanGreenBorder
        BannerSeverity.CONSERVE -> Color(0xFF6B4E1B)
        BannerSeverity.WARNING -> Color(0xFF7C3612)
        BannerSeverity.CRITICAL -> Color(0xFF7A1E26)
    }
    val bannerIconBg = when (bannerState.severity) {
        BannerSeverity.SAFE -> Color(0xFF123C2C)
        BannerSeverity.CONSERVE -> Color(0xFF382B12)
        BannerSeverity.WARNING -> Color(0xFF452210)
        BannerSeverity.CRITICAL -> Color(0xFF48141B)
    }
    val bannerTint = when (bannerState.severity) {
        BannerSeverity.SAFE -> JeevanBrandGreen
        BannerSeverity.CONSERVE -> JeevanFoodYellow
        BannerSeverity.WARNING -> JeevanFuelOrange
        BannerSeverity.CRITICAL -> JeevanMedicalRed
    }
    val bannerTextColor = when (bannerState.severity) {
        BannerSeverity.SAFE -> Color(0xFF86EFAC)
        BannerSeverity.CONSERVE -> Color(0xFFFDE047)
        BannerSeverity.WARNING -> Color(0xFFFDBA74)
        BannerSeverity.CRITICAL -> Color(0xFFFCA5A5)
    }
    val bannerIcon = when (bannerState.severity) {
        BannerSeverity.SAFE -> Icons.Default.Security
        BannerSeverity.CONSERVE -> Icons.Default.WarningAmber
        BannerSeverity.WARNING -> Icons.Default.Warning
        BannerSeverity.CRITICAL -> Icons.Default.Emergency
    }

    // Dynamic resource gauge computations
    val waterDays = waterEstimate.estimatedDaysDrinking
    val waterProgress = (waterDays / 7.0).toFloat().coerceIn(0f, 1f)
    val waterPercent = (waterProgress * 100).toInt()
    val waterSubtitle = if (waterEstimate.drinkingLiters <= 0.0) {
        "0.0 L\n(0%)"
    } else {
        "${String.format(Locale.US, "%.1f", waterDays)} days\n($waterPercent%)"
    }
    val waterColor = when (waterEstimate.status) {
        ResourceStatus.CRITICAL -> JeevanMedicalRed
        ResourceStatus.LIMITED -> JeevanBatteryAmber
        ResourceStatus.SUFFICIENT -> JeevanWaterBlue
    }

    val foodDays = foodEstimate.estimatedDaysRemaining
    val foodProgress = (foodDays / 7.0).toFloat().coerceIn(0f, 1f)
    val foodPercent = (foodProgress * 100).toInt()
    val foodSubtitle = if (foodEstimate.totalMeals <= 0) {
        "0 meals\n(0%)"
    } else {
        "${String.format(Locale.US, "%.1f", foodDays)} days\n($foodPercent%)"
    }
    val foodColor = when (foodEstimate.status) {
        ResourceStatus.CRITICAL -> JeevanMedicalRed
        ResourceStatus.LIMITED -> JeevanFoodYellow
        ResourceStatus.SUFFICIENT -> JeevanFoodYellow
    }

    val batteryPercent = powerEstimate.phoneBatteryPercent
    val powerProgress = (batteryPercent / 100f).coerceIn(0f, 1f)
    val hours = if (dashboardState.batterySaverActive) powerEstimate.estimatedHoursEco else powerEstimate.estimatedHoursNormal
    val powerSubtitle = if (hours >= 24) {
        "${String.format(Locale.US, "%.1f", hours / 24.0)} days\n($batteryPercent%)"
    } else {
        "$hours hrs\n($batteryPercent%)"
    }
    val powerColor = when (powerEstimate.status) {
        ResourceStatus.CRITICAL -> JeevanMedicalRed
        ResourceStatus.LIMITED -> JeevanBatteryAmber
        ResourceStatus.SUFFICIENT -> JeevanBatteryAmber
    }

    val fuelProgress = if (fuelEstimate.hasVehicle) (fuelEstimate.fuelPercent / 100f).coerceIn(0f, 1f) else 0f
    val fuelSubtitle = if (!fuelEstimate.hasVehicle) {
        "No Vehicle\n(N/A)"
    } else {
        "${fuelEstimate.estimatedRangeKm.toInt()} km\n(${fuelEstimate.fuelPercent}%)"
    }
    val fuelColor = when {
        !fuelEstimate.hasVehicle -> JeevanTextMuted
        fuelEstimate.status == ResourceStatus.CRITICAL -> JeevanMedicalRed
        fuelEstimate.status == ResourceStatus.LIMITED -> JeevanFuelOrange
        else -> JeevanFuelOrange
    }

    val (medicalSubtitle, medicalProgress, medicalColor) = remember(medicines) {
        if (medicines.isEmpty()) {
            Triple("0 items\n(0%)", 0f, JeevanMedicalRed)
        } else {
            val minDays = medicines.map {
                if (it.dailyUsage > 0) it.quantity / it.dailyUsage else it.daysRemaining
            }.minOrNull() ?: 0
            val hasCriticalShortage = medicines.any {
                it.isCritical && (if (it.dailyUsage > 0) it.quantity / it.dailyUsage else it.daysRemaining) < 3
            }
            val progress = (minDays / 7f).coerceIn(0f, 1f)
            val percent = (progress * 100).toInt()
            val color = when {
                hasCriticalShortage || minDays < 2 -> JeevanMedicalRed
                minDays < 5 -> JeevanBatteryAmber
                else -> Color(0xFF22C55E)
            }
            Triple("$minDays days\n($percent%)", progress, color)
        }
    }

    val (equipmentSubtitle, equipmentProgress, equipmentColor) = remember(checklistItems) {
        if (checklistItems.isEmpty()) {
            Triple("Not set\n(0%)", 0f, JeevanEquipmentCyan)
        } else {
            val essential = checklistItems.filter { it.isEssential }
            val targetList = if (essential.isNotEmpty()) essential else checklistItems
            val completed = targetList.count { it.isCompleted }
            val total = targetList.size
            val progress = if (total > 0) completed.toFloat() / total.toFloat() else 0f
            val percent = (progress * 100).toInt()
            val label = when {
                progress >= 0.75f -> "Good"
                progress >= 0.40f -> "Fair"
                else -> "Low"
            }
            val color = when {
                progress >= 0.75f -> JeevanEquipmentCyan
                progress >= 0.40f -> JeevanBatteryAmber
                else -> JeevanMedicalRed
            }
            Triple("$label\n($percent%)", progress, color)
        }
    }

    Scaffold(
        containerColor = JeevanBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("dashboard_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top Header: Logo + App Name + Offline Mode + Settings
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        JeevanSetuLogo(size = 38.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Jeevan Setu",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(networkStatusColor)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = networkStatusText,
                                    color = networkStatusColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Quick Language Switcher Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF132230))
                                .border(1.dp, Color(0xFF223548), RoundedCornerShape(20.dp))
                                .clickable { showLanguageSheet = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Language",
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentLanguage.nativeName,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Settings Gear Button
                        IconButton(
                            onClick = onNavigateToSettings,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF16222F))
                                .border(1.dp, JeevanCardBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFFCBD5E1),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 2. Dynamic Status Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(bannerBg)
                        .border(1.2.dp, bannerBorder, RoundedCornerShape(16.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bannerIconBg)
                                .border(1.dp, bannerTint, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = bannerIcon,
                                contentDescription = bannerState.title,
                                tint = bannerTint,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = bannerState.title,
                                color = Color.White,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = bannerState.subtitle,
                                color = bannerTextColor,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // 3. 4 Context Grid Cards (2x2)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ContextTile(
                            icon = Icons.Default.People,
                            title = "People",
                            value = peopleDisplay,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToFamily
                        )
                        ContextTile(
                            icon = Icons.Default.LocationOn,
                            title = "Location",
                            value = locationDisplay,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToMap
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ContextTile(
                            icon = Icons.Default.AccessTime,
                            title = "Time Since",
                            value = timeSinceDisplay,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAssessment
                        )
                        ContextTile(
                            icon = Icons.Default.CalendarMonth,
                            title = "Next Check-in",
                            value = nextCheckInDisplay,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAssessment
                        )
                    }
                }
            }

            // 4. Resource Overview Card (with Circular Progress Gauges)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(JeevanCard)
                        .border(1.dp, JeevanCardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Header row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Resource Overview",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onNavigateToResources() }
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "View All",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "View All",
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Row 1 of 3 gauges: Water, Food, Battery
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            CircularResourceGauge(
                                title = "Water",
                                subtitle = waterSubtitle,
                                progress = waterProgress,
                                color = waterColor,
                                icon = Icons.Default.WaterDrop
                            )
                            CircularResourceGauge(
                                title = "Food",
                                subtitle = foodSubtitle,
                                progress = foodProgress,
                                color = foodColor,
                                icon = Icons.Default.Fastfood
                            )
                            CircularResourceGauge(
                                title = "Battery",
                                subtitle = powerSubtitle,
                                progress = powerProgress,
                                color = powerColor,
                                icon = Icons.Default.BatteryChargingFull
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Row 2 of 3 gauges: Fuel, Medical, Equipment
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            CircularResourceGauge(
                                title = "Fuel",
                                subtitle = fuelSubtitle,
                                progress = fuelProgress,
                                color = fuelColor,
                                icon = Icons.Default.LocalGasStation
                            )
                            CircularResourceGauge(
                                title = "Medical",
                                subtitle = medicalSubtitle,
                                progress = medicalProgress,
                                color = medicalColor,
                                icon = Icons.Default.MedicalServices
                            )
                            CircularResourceGauge(
                                title = "Equipment",
                                subtitle = equipmentSubtitle,
                                progress = equipmentProgress,
                                color = equipmentColor,
                                icon = Icons.Default.Build
                            )
                        }
                    }
                }
            }

            // 5. Motivational Quote Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF101B24))
                        .border(1.dp, Color(0xFF1C2C3B), RoundedCornerShape(14.dp))
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "“",
                            color = Color(0xFF38BDF8),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = "Plan today.",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "A safer tomorrow is in your hands.",
                                color = JeevanTextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Language Selector Bottom Sheet
    if (showLanguageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF131F2C),
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF334B5E)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Select Language / भाषा चुनें",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                AppLanguage.entries.forEach { language ->
                    val isSelected = language == currentLanguage
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF1C3246) else Color.Transparent)
                            .clickable {
                                viewModel.setLanguage(language)
                                showLanguageSheet = false
                            }
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = language.nativeName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = language.englishName,
                                fontSize = 12.sp,
                                color = JeevanTextMuted
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = JeevanBrandGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun ContextTile(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(JeevanCard)
            .border(1.dp, JeevanCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF192533)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF60A5FA),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    color = JeevanTextMuted,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

private enum class BannerSeverity {
    SAFE,
    CONSERVE,
    WARNING,
    CRITICAL
}

private data class BannerState(
    val severity: BannerSeverity,
    val title: String,
    val subtitle: String
)

