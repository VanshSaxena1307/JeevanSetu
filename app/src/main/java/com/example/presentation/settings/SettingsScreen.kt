package com.example.presentation.settings

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Translate
import com.example.domain.model.AppLanguage
import com.example.domain.model.LocalizationData
import androidx.compose.ui.text.TextStyle
import com.example.presentation.components.EmergencyTopBar
import com.example.presentation.components.SafetyDisclaimerCard
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CautionAmber
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
fun SettingsScreen(
    viewModel: JeevanSetuViewModel,
    onBack: () -> Unit,
    onNavigateToSetup: () -> Unit = {}
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val mapRegions by viewModel.mapRegions.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()

    var customLat by remember { mutableStateOf(currentLocation.latitude.toString()) }
    var customLng by remember { mutableStateOf(currentLocation.longitude.toString()) }

    val downloadedRegions = mapRegions.filter { it.isDownloaded }
    val totalOfflineMb = downloadedRegions.sumOf { it.sizeMb }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            EmergencyTopBar(
                title = "SETTINGS & SYSTEM DIAGNOSTICS",
                isOnline = isOnline,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("settings_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Disaster Alert Language Selector Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MintLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = MintDeep
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Disaster Alert Language",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                                )
                                Text(
                                    text = "Audio broadcasts and advisory steps in regional tongue",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            AppLanguage.entries.forEach { language ->
                                val isSelected = language == currentLanguage
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) MintLight else SurfaceWhite)
                                        .clickable { viewModel.setLanguage(language) }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = language.nativeName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = if (isSelected) MintDeep else TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "(${language.displayName})",
                                            fontSize = 12.sp,
                                            color = if (isSelected) MintPrimary else TextSecondary
                                        )
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = MintPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Battery Saver Mode Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(CautionAmber.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CautionAmber)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Emergency Battery Saver", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                                Text(
                                    text = "Minimizes background polls & saves battery",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }
                        }
                        Switch(
                            checked = profile?.batterySaverMode ?: false,
                            onCheckedChange = { viewModel.toggleBatterySaver() },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MintPrimary)
                        )
                    }
                }
            }

            // Offline Storage Metrics
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = MintDeep)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("OFFLINE LOCAL STORAGE", fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Text(
                            text = "Cached Map Regions: ${downloadedRegions.size} of ${mapRegions.size} (${totalOfflineMb} MB)\n" +
                                    "Database Persistence: 100% Offline via Android SQLite Room\n" +
                                    "External Servers: Zero mandatory external API calls required for survival operations.",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, color = TextSecondary)
                        )
                    }
                }
            }

            // Location Simulator / GPS Configuration
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, tint = MintPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("GPS & DISASTER DRILL SIMULATOR", fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Text(
                            text = "Simulate your device in different disaster zones to practice offline evacuation planning and compass navigation.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.setSimulatedLocation(28.6139, 77.2090)
                                    customLat = "28.6139"
                                    customLng = "77.2090"
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MintLight, contentColor = MintDeep),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("DELHI", fontSize = 11.sp, fontWeight = FontWeight.Bold) }

                            Button(
                                onClick = {
                                    viewModel.setSimulatedLocation(19.0760, 72.8777)
                                    customLat = "19.0760"
                                    customLng = "72.8777"
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MintLight, contentColor = MintDeep),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("MUMBAI", fontSize = 11.sp, fontWeight = FontWeight.Bold) }

                            Button(
                                onClick = {
                                    viewModel.setSimulatedLocation(30.0869, 78.2676)
                                    customLat = "30.0869"
                                    customLng = "78.2676"
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MintLight, contentColor = MintDeep),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("RISHIKESH", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            androidx.compose.material3.OutlinedTextField(
                                value = customLat,
                                onValueChange = { customLat = it },
                                label = { Text("Custom Lat") },
                                textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SurfaceWhite,
                                    unfocusedContainerColor = SurfaceWhite,
                                    focusedBorderColor = MintPrimary,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            androidx.compose.material3.OutlinedTextField(
                                value = customLng,
                                onValueChange = { customLng = it },
                                label = { Text("Custom Lng") },
                                textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SurfaceWhite,
                                    unfocusedContainerColor = SurfaceWhite,
                                    focusedBorderColor = MintPrimary,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = {
                                val lat = customLat.toDoubleOrNull() ?: 28.6139
                                val lng = customLng.toDoubleOrNull() ?: 77.2090
                                viewModel.setSimulatedLocation(lat, lng)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("APPLY SIMULATED COORDINATES", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Re-run Setup Wizard Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MintDeep)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("RECONFIGURE SURVIVAL RESERVES", fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Text(
                            text = "Re-launch the 8-step setup wizard to update household dependents, disaster zone, water, food, fuel, or power supplies.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Button(
                            onClick = onNavigateToSetup,
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("RE-RUN SETUP WIZARD", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                SafetyDisclaimerCard()
            }
        }
    }
}
