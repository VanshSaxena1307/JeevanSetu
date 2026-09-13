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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.MintVeryLight
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusCriticalBg
import com.example.ui.theme.StatusCriticalBorder
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerBg
import com.example.ui.theme.StatusDangerBorder
import com.example.ui.theme.StatusElevated
import com.example.ui.theme.StatusElevatedBg
import com.example.ui.theme.StatusElevatedBorder
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusSuccessBorder
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.StatusWarningBorder
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import java.util.Locale

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
    val networkStatusText = if (isOnline) "Online" else "Offline"
    val networkStatusColor = if (isOnline) StatusSuccess else StatusWarning

    val totalPeople = remember(userProfile) {
        val adults = userProfile?.numberOfAdults ?: 0
        val children = userProfile?.numberOfChildren ?: 0
        val elderly = userProfile?.numberOfElderly ?: 0
        val injured = userProfile?.numberOfInjured ?: 0
        adults + children + elderly + injured
    }
    val peopleDisplay = if (totalPeople > 0) "$totalPeople members" else "Set up"

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
            name
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
                days > 0 -> "$days d ${hours % 24}h ago"
                hours > 0 -> "$hours hr ${mins % 60}m ago"
                else -> "$mins min ago"
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
            if (remHours > 0) "In $remHours hr ${remMins}m" else "In $remMins min"
        } else {
            "Ready now"
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
            waterEstimate.drinkingLiters <= 0.0 -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: No Drinking Water",
                    subtitle = "Zero drinking water available. Seek immediate safe hydration or emergency rescue."
                )
            }
            waterEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: Water Depletion",
                    subtitle = "Water reserves will exhaust in ${String.format(Locale.US, "%.1f", waterEstimate.estimatedDaysDrinking)} days. Ration strictly to 1.5L per person per day."
                )
            }
            risk == RiskLevel.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: Immediate Danger",
                    subtitle = assessment?.headline ?: "Extreme hazard detected. Follow urgent safety and evacuation directives."
                )
            }
            foodEstimate.totalMeals <= 0 -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: No Food Remaining",
                    subtitle = "Zero food meals remaining. Adopt emergency rationing and signal for assistance."
                )
            }
            foodEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: Food Shortage",
                    subtitle = "Food supplies critically low (${String.format(Locale.US, "%.1f", foodEstimate.estimatedDaysRemaining)} days remaining)."
                )
            }
            powerEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: Battery Depleted",
                    subtitle = "Device battery at ${powerEstimate.phoneBatteryPercent}%. Enable ultra battery saver immediately."
                )
            }
            fuelEstimate.hasVehicle && fuelEstimate.status == ResourceStatus.CRITICAL -> {
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: Fuel Low",
                    subtitle = "Vehicle fuel at ${fuelEstimate.fuelPercent}%. Safe evacuation range is below recommended threshold."
                )
            }
            medicines.any { it.isCritical && (if (it.dailyUsage > 0) it.quantity / it.dailyUsage else it.daysRemaining) < 2 } -> {
                val med = medicines.first { it.isCritical && (if (it.dailyUsage > 0) it.quantity / it.dailyUsage else it.daysRemaining) < 2 }
                BannerState(
                    severity = BannerSeverity.CRITICAL,
                    title = "Critical: Medicine Depletion",
                    subtitle = "Critical supply of ${med.name} will run out in less than 48 hours."
                )
            }
            risk == RiskLevel.HIGH -> {
                BannerState(
                    severity = BannerSeverity.WARNING,
                    title = "Warning: High Risk Conditions",
                    subtitle = assessment?.headline ?: "High threat conditions detected. Prepare your go-bag and monitor routes."
                )
            }
            fuelEstimate.hasVehicle && !fuelEstimate.canReachSafeLocation -> {
                BannerState(
                    severity = BannerSeverity.WARNING,
                    title = "Warning: Insufficient Fuel",
                    subtitle = "Vehicle range (${fuelEstimate.estimatedRangeKm.toInt()} km) may be insufficient to reach verified safe shelter."
                )
            }
            waterEstimate.status == ResourceStatus.LIMITED -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "Conserve Water Supplies",
                    subtitle = "Water estimated for ${String.format(Locale.US, "%.1f", waterEstimate.estimatedDaysDrinking)} days. Restrict non-essential water usage."
                )
            }
            foodEstimate.status == ResourceStatus.LIMITED -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "Conserve Food Reserves",
                    subtitle = "Food supplies limited to ${String.format(Locale.US, "%.1f", foodEstimate.estimatedDaysRemaining)} days. Consume perishables first."
                )
            }
            fuelEstimate.hasVehicle && fuelEstimate.status == ResourceStatus.LIMITED -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "Conserve Fuel",
                    subtitle = "Vehicle fuel at ${fuelEstimate.fuelPercent}%. Reserve strictly for emergency transit."
                )
            }
            risk == RiskLevel.MODERATE -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "Advisory: Moderate Risk",
                    subtitle = assessment?.headline ?: "Moderate hazards present in your area. Review emergency supplies and safe shelters."
                )
            }
            userProfile == null -> {
                BannerState(
                    severity = BannerSeverity.CONSERVE,
                    title = "Setup Household Profile",
                    subtitle = "Configure family members to calculate accurate water and food survival quotas."
                )
            }
            else -> {
                val minDays = minOf(waterEstimate.estimatedDaysDrinking, foodEstimate.estimatedDaysRemaining)
                BannerState(
                    severity = BannerSeverity.SAFE,
                    title = "Status: Stable & Prepared",
                    subtitle = "Essential supplies estimated to last approximately ${String.format(Locale.US, "%.1f", minDays)} days."
                )
            }
        }
    }

    val bannerBg = when (bannerState.severity) {
        BannerSeverity.SAFE -> StatusSuccessBg
        BannerSeverity.CONSERVE -> StatusWarningBg
        BannerSeverity.WARNING -> StatusElevatedBg
        BannerSeverity.CRITICAL -> StatusCriticalBg
    }
    val bannerBorder = when (bannerState.severity) {
        BannerSeverity.SAFE -> StatusSuccessBorder
        BannerSeverity.CONSERVE -> StatusWarningBorder
        BannerSeverity.WARNING -> StatusElevatedBorder
        BannerSeverity.CRITICAL -> StatusCriticalBorder
    }
    val bannerAccent = when (bannerState.severity) {
        BannerSeverity.SAFE -> StatusSuccess
        BannerSeverity.CONSERVE -> StatusWarning
        BannerSeverity.WARNING -> StatusElevated
        BannerSeverity.CRITICAL -> StatusCritical
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
        "0.0 L\n0%"
    } else {
        "${String.format(Locale.US, "%.1f", waterDays)} days\n$waterPercent%"
    }
    val waterColor = when (waterEstimate.status) {
        ResourceStatus.CRITICAL -> StatusCritical
        ResourceStatus.LIMITED -> StatusWarning
        ResourceStatus.SUFFICIENT -> MintDeep
    }

    val foodDays = foodEstimate.estimatedDaysRemaining
    val foodProgress = (foodDays / 7.0).toFloat().coerceIn(0f, 1f)
    val foodPercent = (foodProgress * 100).toInt()
    val foodSubtitle = if (foodEstimate.totalMeals <= 0) {
        "0 meals\n0%"
    } else {
        "${String.format(Locale.US, "%.1f", foodDays)} days\n$foodPercent%"
    }
    val foodColor = when (foodEstimate.status) {
        ResourceStatus.CRITICAL -> StatusCritical
        ResourceStatus.LIMITED -> StatusWarning
        ResourceStatus.SUFFICIENT -> StatusWarning
    }

    val batteryPercent = powerEstimate.phoneBatteryPercent
    val powerProgress = (batteryPercent / 100f).coerceIn(0f, 1f)
    val hours = if (dashboardState.batterySaverActive) powerEstimate.estimatedHoursEco else powerEstimate.estimatedHoursNormal
    val powerSubtitle = if (hours >= 24) {
        "${String.format(Locale.US, "%.1f", hours / 24.0)} days\n$batteryPercent%"
    } else {
        "$hours hrs\n$batteryPercent%"
    }
    val powerColor = when (powerEstimate.status) {
        ResourceStatus.CRITICAL -> StatusCritical
        ResourceStatus.LIMITED -> StatusElevated
        ResourceStatus.SUFFICIENT -> StatusElevated
    }

    val fuelProgress = if (fuelEstimate.hasVehicle) (fuelEstimate.fuelPercent / 100f).coerceIn(0f, 1f) else 0f
    val fuelSubtitle = if (!fuelEstimate.hasVehicle) {
        "No vehicle\nN/A"
    } else {
        "${fuelEstimate.estimatedRangeKm.toInt()} km\n${fuelEstimate.fuelPercent}%"
    }
    val fuelColor = when {
        !fuelEstimate.hasVehicle -> TextTertiary
        fuelEstimate.status == ResourceStatus.CRITICAL -> StatusCritical
        fuelEstimate.status == ResourceStatus.LIMITED -> StatusElevated
        else -> StatusElevated
    }

    val (medicalSubtitle, medicalProgress, medicalColor) = remember(medicines) {
        if (medicines.isEmpty()) {
            Triple("0 items\n0%", 0f, StatusDanger)
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
                hasCriticalShortage || minDays < 2 -> StatusCritical
                minDays < 5 -> StatusWarning
                else -> StatusSuccess
            }
            Triple("$minDays days\n$percent%", progress, color)
        }
    }

    val (equipmentSubtitle, equipmentProgress, equipmentColor) = remember(checklistItems) {
        if (checklistItems.isEmpty()) {
            Triple("Not set\n0%", 0f, MintDeep)
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
                progress >= 0.75f -> StatusSuccess
                progress >= 0.40f -> StatusWarning
                else -> StatusCritical
            }
            Triple("$label\n$percent%", progress, color)
        }
    }

    Scaffold(
        containerColor = AppBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("dashboard_screen"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. App Header Area
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
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.2.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(networkStatusColor)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = networkStatusText,
                                    color = networkStatusColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Language Switcher
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SurfaceWhite)
                                .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                                .clickable { showLanguageSheet = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Language",
                                    tint = MintDeep,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = currentLanguage.nativeName,
                                    color = TextPrimary,
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
                                .background(SurfaceWhite)
                                .border(1.dp, BorderSubtle, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // 2. Current Safety & Risk Status Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = bannerBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, bannerBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bannerAccent.copy(alpha = 0.15f))
                                .border(1.dp, bannerAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = bannerIcon,
                                contentDescription = bannerState.title,
                                tint = bannerAccent,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = bannerState.title,
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = bannerState.subtitle,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // 3. Immediate Recommended Action (Assessment / Evacuation prompt)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (dashboardState.overallRiskLevel == RiskLevel.CRITICAL || dashboardState.overallRiskLevel == RiskLevel.HIGH) {
                                onNavigateToEvacuation()
                            } else {
                                onNavigateToAssessment()
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MintVeryLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (dashboardState.overallRiskLevel == RiskLevel.CRITICAL) Icons.Default.NearMe else Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = MintDeep,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (dashboardState.overallRiskLevel == RiskLevel.CRITICAL) "Immediate Action: Evacuation Plan" else "Emergency Preparedness Check",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (dashboardState.overallRiskLevel == RiskLevel.CRITICAL) "Inspect verified safe corridors and shelters." else "Assess local conditions to refresh your safety score.",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MintDeep,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // 4. Household Survival / Context Overview (4 Tiles)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ContextTile(
                            icon = Icons.Default.People,
                            title = "Household",
                            value = peopleDisplay,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToFamily
                        )
                        ContextTile(
                            icon = Icons.Default.LocationOn,
                            title = if (dashboardState.isGpsActive) "Location (GPS)" else "Location (Simulated)",
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
                            title = "Last Assessed",
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

            // 5. Resource Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Resource Reserves",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onNavigateToResources() }
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Manage",
                                    color = MintDeep,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Manage",
                                    tint = MintDeep,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Row 1: Water, Food, Battery
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

                        // Row 2: Fuel, Medical, Equipment
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

            // 6. Quick Action Navigation Cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Emergency Essentials",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ActionTile(
                            icon = Icons.Default.NearMe,
                            title = "Evacuation",
                            subtitle = "Route & Transit",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToEvacuation
                        )
                        ActionTile(
                            icon = Icons.Default.LocalHospital,
                            title = "Safe Shelters",
                            subtitle = "Nearest Points",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToSafeLocations
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ActionTile(
                            icon = Icons.Default.PhoneInTalk,
                            title = "Helplines",
                            subtitle = "SOS & Contacts",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToContacts
                        )
                        ActionTile(
                            icon = Icons.Default.Checklist,
                            title = "Go-Bag",
                            subtitle = "Gear Checklist",
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToChecklist
                        )
                    }
                }
            }

            // 7. Preparedness Motto Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MintVeryLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\"",
                            color = MintDeep,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = "Plan ahead with confidence.",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "A safer household begins with reliable preparation.",
                                color = TextSecondary,
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
            containerColor = SurfaceWhite,
            dragHandle = { BottomSheetDefaults.DragHandle(color = BorderSubtle) }
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
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                AppLanguage.entries.forEach { language ->
                    val isSelected = language == currentLanguage
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MintLight else Color.Transparent)
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
                                color = TextPrimary
                            )
                            Text(
                                text = language.englishName,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MintDeep,
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
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintVeryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MintDeep,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ActionTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintVeryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MintDeep,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 11.sp
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
