package com.example.presentation.predictions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.predictions.UsageForecastChartCard
import com.example.ui.theme.JeevanBatteryAmber
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanFuelOrange
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.ResourceStatus
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanBrandGreenBg
import com.example.ui.theme.JeevanBrandGreenBorder
import com.example.ui.theme.JeevanRedBg
import com.example.ui.theme.JeevanRedBorder
import com.example.ui.theme.JeevanTextMuted
import com.example.ui.theme.JeevanWaterBlue
import java.util.Locale

@Composable
fun SmartPredictionsView(
    viewModel: JeevanSetuViewModel? = null,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val waterEstimate = viewModel?.waterEstimate?.collectAsStateWithLifecycle()?.value
    val foodEstimate = viewModel?.foodEstimate?.collectAsStateWithLifecycle()?.value
    val powerEstimate = viewModel?.powerEstimate?.collectAsStateWithLifecycle()?.value
    val fuelEstimate = viewModel?.fuelEstimate?.collectAsStateWithLifecycle()?.value

    val waterDaysStr = waterEstimate?.let {
        if (it.estimatedDaysDrinking <= 0.0) "0.0 days"
        else String.format(Locale.US, "%.1f days", it.estimatedDaysDrinking)
    } ?: "0.0 days"

    val foodDaysStr = foodEstimate?.let {
        if (it.estimatedDaysRemaining <= 0.0) "0.0 days"
        else String.format(Locale.US, "%.1f days", it.estimatedDaysRemaining)
    } ?: "0.0 days"

    val powerDaysStr = powerEstimate?.let {
        if (it.estimatedHoursEco >= 24) String.format(Locale.US, "%.1f days", it.estimatedHoursEco / 24.0)
        else "${it.estimatedHoursEco} hrs"
    } ?: "0 hrs"

    val fuelDaysStr = fuelEstimate?.let {
        if (it.hasVehicle) "${it.estimatedRangeKm.toInt()} km"
        else "No vehicle"
    } ?: "N/A"

    val isWaterCritical = waterEstimate?.status == ResourceStatus.CRITICAL
    val isFoodCritical = foodEstimate?.status == ResourceStatus.CRITICAL
    val isPowerCritical = powerEstimate?.status == ResourceStatus.CRITICAL
    val isFuelCritical = fuelEstimate?.hasVehicle == true && fuelEstimate.status == ResourceStatus.CRITICAL

    val isAnyCritical = isWaterCritical || isFoodCritical || isPowerCritical || isFuelCritical

    val bannerBg = if (isAnyCritical) JeevanRedBg else JeevanBrandGreenBg
    val bannerBorder = if (isAnyCritical) JeevanRedBorder else JeevanBrandGreenBorder
    val bannerIconTint = if (isAnyCritical) Color(0xFFEF4444) else JeevanBrandGreen
    val bannerIconBoxBg = if (isAnyCritical) Color(0xFF45181C) else Color(0xFF0F382A)

    val (bannerTitle, bannerSubtitle) = when {
        isWaterCritical -> "CONSERVE WATER" to "Drinking water is critical (${waterDaysStr} remaining). Ration strictly."
        isFoodCritical -> "FOOD SHORTAGE ALERT" to "Food reserves are depleted or critical (${foodDaysStr} remaining)."
        isPowerCritical -> "POWER CRITICAL" to "Power depleted or critical (${powerDaysStr} remaining). Enable eco mode."
        isFuelCritical -> "LOW EVACUATION FUEL" to "Vehicle fuel is critical (${fuelEstimate?.fuelPercent}%). Reserve for evacuation."
        else -> "RESOURCES MONITORED" to "Provisions meet survival requirements based on your household size."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JeevanBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column {
                Text(
                    text = "Projections & Analysis",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Calculated from current inventory and household demand.",
                    color = JeevanTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Warning / Status Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(bannerBg)
                .border(1.dp, bannerBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bannerIconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isAnyCritical) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = if (isAnyCritical) "Critical" else "Stable",
                        tint = bannerIconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = bannerTitle,
                        color = bannerIconTint,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bannerSubtitle,
                        color = if (isAnyCritical) Color(0xFFFCA5A5) else Color(0xFFA7F3D0),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4 Grid Tiles (2x2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PredictionStatTile(
                icon = Icons.Default.WaterDrop,
                iconColor = JeevanWaterBlue,
                value = waterDaysStr,
                label = "Water supply",
                modifier = Modifier.weight(1f)
            )
            PredictionStatTile(
                icon = Icons.Default.Fastfood,
                iconColor = Color(0xFF22C55E),
                value = foodDaysStr,
                label = "Food supply",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PredictionStatTile(
                icon = Icons.Default.BatteryChargingFull,
                iconColor = JeevanBatteryAmber,
                value = powerDaysStr,
                label = "Battery/power",
                modifier = Modifier.weight(1f)
            )
            PredictionStatTile(
                icon = Icons.Default.LocalGasStation,
                iconColor = JeevanFuelOrange,
                value = fuelDaysStr,
                label = "Fuel supply",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Usage Forecast Line Chart
        UsageForecastChartCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Key Insights
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(JeevanCard)
                .border(1.dp, JeevanCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Key Insights",
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Key Insights & Directives",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val dynamicInsights = buildList {
                    waterEstimate?.let {
                        if (it.status == ResourceStatus.CRITICAL) {
                            add(it.recommendation.ifBlank { "Water critical. Limit strictly to drinking minimum (2L/day)." })
                        } else {
                            add("Water reserve estimated for ${waterDaysStr} based on household size.")
                        }
                    }
                    foodEstimate?.let {
                        if (it.status == ResourceStatus.CRITICAL) {
                            add(it.recommendation.ifBlank { "Food reserves critically low. Prioritize perishable items first." })
                        } else {
                            add("Food supplies estimated to sustain household for ${foodDaysStr}.")
                        }
                    }
                    powerEstimate?.let {
                        if (it.conservationTips.isNotEmpty()) {
                            add(it.conservationTips.first())
                        }
                    }
                    fuelEstimate?.let {
                        if (it.hasVehicle) {
                            add("Vehicle fuel range: approx. ${it.estimatedRangeKm.toInt()} km. Keep reserve intact for evacuation.")
                        } else {
                            add("No vehicle configured. Focus readiness on foot / public evacuation.")
                        }
                    }
                }.ifEmpty {
                    listOf(
                        "Maintain at least 3-day drinking water reserve for each person.",
                        "Prioritize consuming perishable foods before sealed survival rations.",
                        "Keep power banks fully charged and switch phone to ultra-saver mode when off-grid.",
                        "Verify emergency equipment readiness regularly."
                    )
                }

                dynamicInsights.forEach { insight ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("• ", color = JeevanTextMuted, fontSize = 14.sp)
                        Text(
                            text = insight,
                            color = Color(0xFFE2E8F0),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PredictionStatTile(
    icon: ImageVector,
    iconColor: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(JeevanCard)
            .border(1.dp, JeevanCardBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = JeevanTextMuted,
                fontSize = 11.sp
            )
        }
    }
}
