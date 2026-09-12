package com.example.presentation.family

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
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.TextStyle
import com.example.presentation.components.EmergencyTopBar
import com.example.presentation.components.SafetyDisclaimerCard
import com.example.presentation.components.tacticalTextFieldColors
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.RescueCyan
import com.example.ui.theme.SafetyGreen
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.WarningOrange

@Composable
fun FamilyProfileScreen(
    viewModel: JeevanSetuViewModel,
    onBack: () -> Unit
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()

    var adults by remember(profile) { mutableIntStateOf(profile?.numberOfAdults ?: 2) }
    var children by remember(profile) { mutableIntStateOf(profile?.numberOfChildren ?: 0) }
    var elderly by remember(profile) { mutableIntStateOf(profile?.numberOfElderly ?: 0) }
    var injured by remember(profile) { mutableIntStateOf(profile?.numberOfInjured ?: 0) }
    var specialNotes by remember(profile) { mutableStateOf(profile?.specialNeeds ?: "") }

    val totalMembers = adults + children + elderly
    val dailyWaterNeededLiters = totalMembers * 3.0
    val dailyMealsNeeded = totalMembers * 3

    Scaffold(
        topBar = {
            EmergencyTopBar(
                title = "FAMILY & VULNERABILITIES",
                isOnline = isOnline,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("family_profile_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate800),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = CautionAmber)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DEPENDENT DEMOGRAPHICS ENGINE", fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "Configuring your family head count directly scales all offline survival math. Children and elderly individuals also factor heavily into evacuation risk calculations.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            }

            // Real-time Consumption Calculation Preview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, WarningOrange)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TOTAL DEPENDENTS", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("$totalMembers People", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = Color.White))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("DAILY WATER REQUIREMENT", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                            Text("${String.format("%.1f", dailyWaterNeededLiters)} Liters / Day", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = RescueCyan))
                        }
                    }
                }
            }

            // Stepper Rows
            item {
                MemberCountStepper(
                    label = "Adults (18 - 65 yrs)",
                    count = adults,
                    icon = Icons.Default.Person,
                    color = SafetyGreen,
                    onIncrement = { adults++ },
                    onDecrement = { if (adults > 1) adults-- }
                )
            }

            item {
                MemberCountStepper(
                    label = "Children (< 18 yrs)",
                    count = children,
                    icon = Icons.Default.ChildCare,
                    color = RescueCyan,
                    onIncrement = { children++ },
                    onDecrement = { if (children > 0) children-- }
                )
            }

            item {
                MemberCountStepper(
                    label = "Elderly (> 65 yrs)",
                    count = elderly,
                    icon = Icons.Default.Elderly,
                    color = CautionAmber,
                    onIncrement = { elderly++ },
                    onDecrement = { if (elderly > 0) elderly-- }
                )
            }

            item {
                MemberCountStepper(
                    label = "Injured / Non-Ambulatory",
                    count = injured,
                    icon = Icons.Default.Healing,
                    color = EmergencyRed,
                    onIncrement = { injured++ },
                    onDecrement = { if (injured > 0) injured-- }
                )
            }

            item {
                OutlinedTextField(
                    value = specialNotes,
                    onValueChange = { specialNotes = it },
                    label = { Text("Special Needs & Medical Equipment (e.g. Wheelchair, Oxygen, Insulin)", color = Color(0xFF94A3B8)) },
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    colors = tacticalTextFieldColors(focusedBorderColor = WarningOrange),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )
            }

            item {
                Button(
                    onClick = {
                        viewModel.updateFamilyCounts(adults, children, elderly, injured, specialNotes)
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_family_profile_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WarningOrange)
                ) {
                    Text("SAVE & UPDATE SURVIVAL MODELS", fontWeight = FontWeight.Bold)
                }
            }

            item {
                SafetyDisclaimerCard()
            }
        }
    }
}

@Composable
fun MemberCountStepper(
    label: String,
    count: Int,
    icon: ImageVector,
    color: Color,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onDecrement,
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-", fontWeight = FontWeight.Black, fontSize = 18.sp)
                }

                Text(
                    text = "$count",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Button(
                    onClick = onIncrement,
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {
                    Text("+", fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
            }
        }
    }
}
