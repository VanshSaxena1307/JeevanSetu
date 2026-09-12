package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.EvacuationAction
import com.example.domain.model.ResourceStatus
import com.example.domain.model.RiskLevel
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MintDeep
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
import com.example.ui.theme.StatusInfoBg
import com.example.ui.theme.StatusInfoBorder
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusSuccessBorder
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.StatusWarningBorder
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyTopBar(
    title: String,
    isOnline: Boolean,
    batterySaverActive: Boolean = false,
    onToggleBatterySaver: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isOnline) StatusSuccess else StatusWarning)
                    )
                    Text(
                        text = if (isOnline) "Online" else "Offline Mode",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isOnline) StatusSuccess else StatusWarning
                        )
                    )
                }
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            }
        },
        actions = {
            if (onToggleBatterySaver != null) {
                IconButton(
                    onClick = onToggleBatterySaver,
                    modifier = Modifier.testTag("battery_saver_toggle_button")
                ) {
                    Icon(
                        imageVector = if (batterySaverActive) Icons.Default.Bolt else Icons.Default.BatteryChargingFull,
                        contentDescription = "Battery Saver",
                        tint = if (batterySaverActive) StatusWarning else TextSecondary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceWhite,
            titleContentColor = TextPrimary
        ),
        modifier = Modifier.border(width = 0.5.dp, color = BorderSubtle)
    )
}

@Composable
fun RiskBadge(level: RiskLevel) {
    val (bgColor, textColor, borderColor) = when (level) {
        RiskLevel.LOW -> Triple(StatusSuccessBg, StatusSuccess, StatusSuccessBorder)
        RiskLevel.MODERATE -> Triple(StatusWarningBg, StatusWarning, StatusWarningBorder)
        RiskLevel.HIGH -> Triple(StatusDangerBg, StatusDanger, StatusDangerBorder)
        RiskLevel.CRITICAL -> Triple(StatusCriticalBg, StatusCritical, StatusCriticalBorder)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = level.badgeText,
            color = textColor,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun EvacuationActionCard(action: EvacuationAction) {
    val (bgColor, accentColor, borderColor) = when (action) {
        EvacuationAction.SHELTER_IN_PLACE -> Triple(StatusSuccessBg, StatusSuccess, StatusSuccessBorder)
        EvacuationAction.PREPARE_TO_EVACUATE -> Triple(StatusWarningBg, StatusWarning, StatusWarningBorder)
        EvacuationAction.EVACUATE_IF_SAFE -> Triple(StatusElevatedBg, StatusElevated, StatusElevatedBorder)
        EvacuationAction.IMMEDIATE_DANGER -> Triple(StatusCriticalBg, StatusCritical, StatusCriticalBorder)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("evacuation_action_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "ACTION DIRECTIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = action.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun ResourceStatusPill(status: ResourceStatus, label: String) {
    val (bgColor, textColor, borderColor) = when (status) {
        ResourceStatus.SUFFICIENT -> Triple(StatusSuccessBg, StatusSuccess, StatusSuccessBorder)
        ResourceStatus.LIMITED -> Triple(StatusWarningBg, StatusWarning, StatusWarningBorder)
        ResourceStatus.CRITICAL -> Triple(StatusDangerBg, StatusDanger, StatusDangerBorder)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "$label: ${status.label}",
            color = textColor,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
fun SafetyDisclaimerCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("safety_disclaimer_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MintVeryLight),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MintDeep,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Decision Support Notice: This is guidance based on user input and local data. It does not replace official emergency authorities. Always follow official evacuation orders when available.",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
            )
        }
    }
}
