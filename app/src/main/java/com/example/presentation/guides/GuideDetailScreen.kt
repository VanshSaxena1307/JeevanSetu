package com.example.presentation.guides

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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DisasterGuide
import com.example.presentation.components.EmergencyTopBar
import com.example.presentation.components.SafetyDisclaimerCard
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.SafetyGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange

@Composable
fun GuideDetailScreen(
    guide: DisasterGuide,
    isOnline: Boolean,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            EmergencyTopBar(
                title = guide.title.uppercase(),
                isOnline = isOnline,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("guide_detail_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // High Risk Threshold Notice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EmergencyRed.copy(alpha = 0.08f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CRITICAL RISK THRESHOLD",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black, color = EmergencyRed)
                            )
                            Text(
                                text = guide.highRiskThreshold,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, lineHeight = 18.sp)
                            )
                        }
                    }
                }
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Text(
                        text = guide.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = TextPrimary),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Phases: During, Before, After
            if (guide.duringSteps.isNotEmpty()) {
                item {
                    PhaseCard(
                        phaseName = "DURING",
                        title = "Immediate Crisis Actions (SURVIVAL)",
                        steps = guide.duringSteps
                    )
                }
            }

            if (guide.beforeSteps.isNotEmpty()) {
                item {
                    PhaseCard(
                        phaseName = "BEFORE",
                        title = "Preparation & Hardening",
                        steps = guide.beforeSteps
                    )
                }
            }

            if (guide.afterSteps.isNotEmpty()) {
                item {
                    PhaseCard(
                        phaseName = "AFTER",
                        title = "Post-Impact Assessment & Safety",
                        steps = guide.afterSteps
                    )
                }
            }

            // Do's & Don'ts
            item {
                Text(
                    text = "CRITICAL DO'S AND DON'TS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                )
            }

            item {
                DosAndDontsCard(dos = guide.doList, donts = guide.dontList)
            }

            item {
                SafetyDisclaimerCard()
            }
        }
    }
}

@Composable
fun PhaseCard(
    phaseName: String,
    title: String,
    steps: List<String>
) {
    val phaseColor = when (phaseName) {
        "DURING" -> EmergencyRed
        "BEFORE" -> WarningOrange
        else -> SafetyGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, phaseColor.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(phaseColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(phaseName, color = phaseColor, fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
            }

            steps.forEachIndexed { idx, step ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Text(
                        text = "${idx + 1}. ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = phaseColor)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp, color = TextPrimary)
                    )
                }
            }
        }
    }
}

@Composable
fun DosAndDontsCard(dos: List<String>, donts: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // DO's
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, SafetyGreen.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SafetyGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("MANDATORY DO'S", fontWeight = FontWeight.Bold, color = SafetyGreen)
                }
                dos.forEach { d ->
                    Text("✓ $d", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, lineHeight = 18.sp))
                }
            }
        }

        // DON'TS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmergencyRed.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("STRICT DON'TS (FATAL MISTAKES)", fontWeight = FontWeight.Bold, color = EmergencyRed)
                }
                donts.forEach { d ->
                    Text("✗ $d", style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, lineHeight = 18.sp))
                }
            }
        }
    }
}
