package com.example.presentation.assessment

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.DisasterType
import com.example.domain.model.RiskLevel
import com.example.domain.model.RiskResult
import com.example.domain.model.RouteStatus
import com.example.presentation.components.EmergencyTopBar
import com.example.presentation.components.EvacuationActionCard
import com.example.presentation.components.RiskBadge
import com.example.presentation.components.SafetyDisclaimerCard
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.SafetyGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange

@Composable
fun AssessmentScreen(
    viewModel: JeevanSetuViewModel,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToGuides: () -> Unit
) {
    val draft by viewModel.assessmentDraft.collectAsStateWithLifecycle()
    val riskResult by viewModel.currentRiskResult.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    var currentStep by remember { mutableIntStateOf(if (riskResult != null) 5 else 1) }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            EmergencyTopBar(
                title = "EMERGENCY ASSESSMENT",
                isOnline = isOnline,
                onBack = {
                    if (currentStep > 1 && currentStep < 5) {
                        currentStep--
                    } else {
                        onBack()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AppBackground)
                .testTag("assessment_screen")
        ) {
            // Step Progress Indicator
            if (currentStep < 5) {
                StepIndicator(currentStep = currentStep, totalSteps = 4)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (currentStep) {
                    1 -> {
                        item {
                            Text(
                                text = "STEP 1: SELECT DISASTER TYPE",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintDeep
                                )
                            )
                            Text(
                                text = "Choose the primary ongoing hazard in your area to adapt specific danger questions.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        DisasterType.values().forEach { type ->
                            item {
                                DisasterTypeSelectCard(
                                    type = type,
                                    isSelected = draft.disasterType == type,
                                    onClick = {
                                        viewModel.updateAssessmentDraft { copy(disasterType = type) }
                                    }
                                )
                            }
                        }

                        item {
                            Button(
                                onClick = { currentStep = 2 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("assessment_step1_next_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                            ) {
                                Text("CONTINUE TO IMMEDIATE THREATS", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    2 -> {
                        item {
                            Text(
                                text = "STEP 2: IMMEDIATE ENVIRONMENT",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintDeep
                                )
                            )
                            Text(
                                text = "Questions tailored for: ${draft.disasterType.displayName}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        when (draft.disasterType) {
                            DisasterType.FLOOD, DisasterType.FLASH_FLOOD -> {
                                item {
                                    ToggleCard(
                                        title = "Is floodwater actively entering your building?",
                                        subtitle = "Water coming under doors or through floorboards",
                                        checked = draft.isWaterEntering,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isWaterEntering = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Is the water level actively rising?",
                                        subtitle = "Marked elevation increase over the last 30 minutes",
                                        checked = draft.isWaterRising,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isWaterRising = it) } }
                                    )
                                }
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "Approximate Water Depth Outside / Ground Floor: ${draft.waterLevelInches} inches",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                                            )
                                            Text(
                                                text = if (draft.waterLevelInches > 12) "CRITICAL: > 1 foot of water can sweep cars and adults!" else "Keep measuring water elevation.",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (draft.waterLevelInches > 12) EmergencyRed else CautionAmber
                                                )
                                            )
                                            Slider(
                                                value = draft.waterLevelInches.toFloat(),
                                                onValueChange = { viewModel.updateAssessmentDraft { copy(waterLevelInches = it.toInt()) } },
                                                valueRange = 0f..48f,
                                                steps = 8,
                                                colors = SliderDefaults.colors(thumbColor = MintPrimary, activeTrackColor = MintPrimary)
                                            )
                                        }
                                    }
                                }
                                item {
                                    ToggleCard(
                                        title = "Is main electricity active in submerged areas?",
                                        subtitle = "Severe shock and electrocution hazard",
                                        checked = draft.isElectricityActiveInWater,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isElectricityActiveInWater = it) } }
                                    )
                                }
                            }

                            DisasterType.FIRE -> {
                                item {
                                    ToggleCard(
                                        title = "Is fire inside or directly adjacent to the building?",
                                        subtitle = "Visible flames within 50 meters or inside walls",
                                        checked = draft.isFireInsideOrAdjacent,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isFireInsideOrAdjacent = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Is smoke or fumes entering your shelter?",
                                        subtitle = "Smoke inhalation is the primary cause of fatalities",
                                        checked = draft.isSmokeEntering,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isSmokeEntering = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Are primary ground exits safe and unobstructed?",
                                        subtitle = "Passageways clear of intense heat and flames",
                                        checked = draft.isSafeExitAvailable,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isSafeExitAvailable = it) } }
                                    )
                                }
                            }

                            DisasterType.EARTHQUAKE, DisasterType.BUILDING_COLLAPSE -> {
                                item {
                                    ToggleCard(
                                        title = "Has the building partially collapsed?",
                                        subtitle = "Fallen ceilings, collapsed staircases, or fallen exterior walls",
                                        checked = draft.isPartiallyCollapsed,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isPartiallyCollapsed = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Are there large visible cracks or building tilt?",
                                        subtitle = "Shearing cracks across concrete pillars or foundations",
                                        checked = draft.hasVisibleCracksOrTilt,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(hasVisibleCracksOrTilt = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Do you smell a natural gas leak or chemical fumes?",
                                        subtitle = "Rotten egg odor or hissing pipes (do not flick switches)",
                                        checked = draft.isGasLeakSuspected,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isGasLeakSuspected = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Are strong seismic aftershocks continuing?",
                                        subtitle = "Secondary tremors further destabilizing masonry",
                                        checked = draft.isAftershocksContinuing,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isAftershocksContinuing = it) } }
                                    )
                                }
                            }

                            DisasterType.LANDSLIDE -> {
                                item {
                                    ToggleCard(
                                        title = "Are you situated near a steep or unstable slope?",
                                        subtitle = "Hillside, ravine, or mudflow basin",
                                        checked = draft.isNearUnstableSlope,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isNearUnstableSlope = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Is soil, rock debris, or mud visibly creeping/moving?",
                                        subtitle = "Trees tilting, rumbling noises, or ground fissures",
                                        checked = draft.isSoilOrDebrisMoving,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isSoilOrDebrisMoving = it) } }
                                    )
                                }
                                item {
                                    ToggleCard(
                                        title = "Is torrential rain continuing without pause?",
                                        subtitle = "Saturation triggers catastrophic slope liquidation",
                                        checked = draft.isHeavyRainContinuing,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isHeavyRainContinuing = it) } }
                                    )
                                }
                            }

                            else -> {
                                item {
                                    ToggleCard(
                                        title = "Is the building structurally damaged?",
                                        subtitle = "Compromised roof, shattered windows, or damaged walls",
                                        checked = draft.isBuildingDamaged,
                                        onCheckedChange = { viewModel.updateAssessmentDraft { copy(isBuildingDamaged = it) } }
                                    )
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { currentStep = 3 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("assessment_step2_next_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                            ) {
                                Text("CONTINUE TO VULNERABILITIES", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    3 -> {
                        item {
                            Text(
                                text = "STEP 3: PEOPLE & VULNERABILITIES",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintDeep
                                )
                            )
                            Text(
                                text = "Accounting for people who need special assistance during rescue or evacuation.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        item {
                            ToggleCard(
                                title = "Is anyone trapped or unable to move?",
                                subtitle = "Pinned by debris, trapped in an attic or room without exit",
                                checked = draft.isAnyoneTrapped,
                                onCheckedChange = { viewModel.updateAssessmentDraft { copy(isAnyoneTrapped = it) } }
                            )
                        }

                        item {
                            ToggleCard(
                                title = "Are there children present?",
                                subtitle = "Requiring physical guidance and carry support",
                                checked = draft.hasChildren,
                                onCheckedChange = { viewModel.updateAssessmentDraft { copy(hasChildren = it) } }
                            )
                        }

                        item {
                            ToggleCard(
                                title = "Are there elderly family members present?",
                                subtitle = "Reduced walking speed or mobility limitations",
                                checked = draft.hasElderly,
                                onCheckedChange = { viewModel.updateAssessmentDraft { copy(hasElderly = it) } }
                            )
                        }

                        item {
                            ToggleCard(
                                title = "Does anyone require critical medical devices or special care?",
                                subtitle = "Oxygen tanks, wheelchair, dialysis, or insulin refrigeration",
                                checked = draft.hasSpecialAssistanceNeeds,
                                onCheckedChange = { viewModel.updateAssessmentDraft { copy(hasSpecialAssistanceNeeds = it) } }
                            )
                        }

                        item {
                            Button(
                                onClick = { currentStep = 4 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("assessment_step3_next_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                            ) {
                                Text("CONTINUE TO ROUTE CONDITIONS", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    4 -> {
                        item {
                            Text(
                                text = "STEP 4: EVACUATION ROUTE STATUS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintDeep
                                )
                            )
                            Text(
                                text = "CRITICAL: The system evaluates route hazards so you are never sent blindly into floodwaters or fire.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        RouteStatus.values().forEach { route ->
                            item {
                                RouteStatusCard(
                                    status = route,
                                    isSelected = draft.routeStatus == route,
                                    onClick = {
                                        viewModel.updateAssessmentDraft { copy(routeStatus = route) }
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.runAssessment()
                                    currentStep = 5
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .testTag("calculate_risk_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                            ) {
                                Text("CALCULATE RISK & GENERATE RECOMMENDATION", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    5 -> {
                        // Results View
                        val result = riskResult
                        if (result != null) {
                            item {
                                ResultScoreCard(result = result)
                            }

                            item {
                                EvacuationActionCard(action = result.evacuationAction)
                            }

                            // Transparent Explanation Section
                            item {
                                Text(
                                    text = "WHY THIS RECOMMENDATION WAS GIVEN",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        letterSpacing = 1.2.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                )
                            }

                            result.dangerFactors.forEach { factor ->
                                item {
                                    DangerFactorRow(factor = factor)
                                }
                            }

                            if (result.dangerFactors.isEmpty()) {
                                item {
                                    Text(
                                        text = "No severe structural or environmental danger factors triggered.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = SafetyGreen)
                                    )
                                }
                            }

                            item {
                                RecommendationDetailCard(
                                    headline = result.headline,
                                    recommendation = result.recommendation,
                                    safeLocationNote = result.safeLocationNote
                                )
                            }

                            if (result.evacuationCautions.isNotEmpty()) {
                                item {
                                    CautionListCard(cautions = result.evacuationCautions)
                                }
                            }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { currentStep = 1 },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("RE-EVALUATE", color = TextPrimary)
                                    }
                                    Button(
                                        onClick = onNavigateToMap,
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                                    ) {
                                        Text("OPEN MAP", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }

                            item {
                                SafetyDisclaimerCard()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..totalSteps) {
            val isPassed = step < currentStep
            val isCurrent = step == currentStep
            val color = when {
                isPassed -> MintPrimary
                isCurrent -> MintDeep
                else -> BorderSubtle
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = step.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) Color.White else TextSecondary
                            )
                        )
                    }
                }
                if (step < totalSteps) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(2.dp)
                            .background(if (isPassed) MintPrimary else BorderSubtle)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun DisasterTypeSelectCard(
    type: DisasterType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("disaster_type_${type.name.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MintLight else SurfaceWhite
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) MintPrimary else BorderSubtle
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = type.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MintDeep else TextPrimary
                    )
                )
                Text(
                    text = type.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary
                    )
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MintPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun RouteStatusCard(
    status: RouteStatus,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("route_status_${status.name.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MintLight else SurfaceWhite
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) MintPrimary else BorderSubtle
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = status.label,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) MintDeep else TextPrimary
                    )
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MintPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun ToggleCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (checked) MintPrimary else BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary
                    )
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MintPrimary,
                    uncheckedTrackColor = BorderSubtle
                )
            )
        }
    }
}

@Composable
fun ResultScoreCard(result: RiskResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CALCULATED OFFLINE RISK SCORE",
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = 1.2.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${result.score}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(result.riskLevel.hexColor)
                    )
                )
                Text(
                    text = "/100",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    ),
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            RiskBadge(level = result.riskLevel)
        }
    }
}

@Composable
fun DangerFactorRow(factor: com.example.domain.model.DangerFactor) {
    val color = Color(factor.severity.hexColor)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceWhite)
            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = factor.description,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary)
            )
            Text(
                text = "Category: ${factor.category} • Severity: ${factor.severity.label}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            )
        }
    }
}

@Composable
fun RecommendationDetailCard(
    headline: String,
    recommendation: String,
    safeLocationNote: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = headline,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
            Text(
                text = recommendation,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = TextPrimary
                )
            )
            if (safeLocationNote != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MintLight)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "📍 $safeLocationNote",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = MintDeep)
                    )
                }
            }
        }
    }
}

@Composable
fun CautionListCard(cautions: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4F4)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD5D5))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = EmergencyRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CRITICAL SAFETY CAUTIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmergencyRed
                    )
                )
            }
            cautions.forEach { caution ->
                Text(
                    text = "• $caution",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF4A1A1A),
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
