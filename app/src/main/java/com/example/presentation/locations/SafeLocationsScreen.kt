package com.example.presentation.locations

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.db.SafeLocationEntity
import com.example.presentation.components.EmergencyTopBar
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
import com.example.utils.GeoLocationUtils

@Composable
fun SafeLocationsScreen(
    viewModel: JeevanSetuViewModel,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit
) {
    val context = LocalContext.current
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val safeLocations by viewModel.safeLocations.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val targetSafeLocation by viewModel.targetSafeLocation.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            EmergencyTopBar(
                title = "SAFE LOCATIONS & SHELTERS",
                isOnline = isOnline,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("safe_locations_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VERIFIED COMMUNITY SHELTERS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    )
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MintPrimary, contentColor = Color.White)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ADD SHELTER")
                    }
                }
            }

            if (safeLocations.isEmpty()) {
                item {
                    Text(
                        text = "No safe locations registered. Tap ADD SHELTER to save community centres, high ground, or family addresses.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            items(safeLocations) { loc ->
                val dist = GeoLocationUtils.calculateDistanceKm(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    loc.latitude,
                    loc.longitude
                )
                val bearing = GeoLocationUtils.calculateBearing(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    loc.latitude,
                    loc.longitude
                )
                val isTarget = targetSafeLocation?.id == loc.id

                SafeLocationCard(
                    location = loc,
                    distanceKm = dist,
                    bearingDeg = bearing,
                    isTarget = isTarget,
                    onSelectTarget = {
                        viewModel.setTargetSafeLocation(loc)
                        onNavigateToMap()
                    },
                    onCall = {
                        if (loc.contactPhone.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${loc.contactPhone}"))
                            context.startActivity(intent)
                        }
                    },
                    onDelete = { viewModel.deleteSafeLocation(loc) }
                )
            }

            item {
                SafetyDisclaimerCard()
            }
        }
    }

    if (showAddDialog) {
        AddSafeLocationDialog(
            currentLat = currentLocation.latitude,
            currentLng = currentLocation.longitude,
            onDismiss = { showAddDialog = false },
            onAdd = { name, lat, lng, cat, cap, phone, notes ->
                viewModel.addSafeLocation(name, lat, lng, cat, cap, phone, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun SafeLocationCard(
    location: SafeLocationEntity,
    distanceKm: Double,
    bearingDeg: Float,
    isTarget: Boolean,
    onSelectTarget: () -> Unit,
    onCall: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryColor = when (location.category) {
        "HOSPITAL" -> EmergencyRed
        "SHELTER" -> WarningOrange
        "CAMP" -> RescueCyan
        else -> SafetyGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(
            if (isTarget) 1.5.dp else 1.dp,
            if (isTarget) MintPrimary else BorderSubtle
        )
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
                            .background(categoryColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = categoryColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = location.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
                        Text(
                            text = "${location.category} • ${location.capacity}",
                            style = MaterialTheme.typography.labelSmall.copy(color = categoryColor, fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
                if (!location.isPreloaded) {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = TextSecondary)
                    }
                }
            }

            Text(
                text = "Straight-line: ~${String.format("%.1f", distanceKm)} km • Compass: ${String.format("%.0f", bearingDeg)}° (${GeoLocationUtils.bearingToCardinal(bearingDeg)})",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MintDeep)
            )

            Text(
                text = location.notes,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSelectTarget,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTarget) MintDeep else MintPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isTarget) "TARGET ACTIVE" else "SET TARGET")
                }

                if (location.contactPhone.isNotBlank()) {
                    OutlinedButton(
                        onClick = onCall,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("CALL")
                    }
                }
            }
        }
    }
}

@Composable
fun AddSafeLocationDialog(
    currentLat: Double,
    currentLng: Double,
    onDismiss: () -> Unit,
    onAdd: (name: String, lat: Double, lng: Double, cat: String, cap: String, phone: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf(String.format("%.4f", currentLat + 0.02)) }
    var lngText by remember { mutableStateOf(String.format("%.4f", currentLng + 0.02)) }
    var category by remember { mutableStateOf("SHELTER") }
    var capacity by remember { mutableStateOf("Medium (100+)") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        titleContentColor = TextPrimary,
        textContentColor = TextPrimary,
        title = { Text("Add Safe Location", fontWeight = FontWeight.Bold, color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                androidx.compose.material3.OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Location / Shelter Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = MintPrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    androidx.compose.material3.OutlinedTextField(
                        value = latText,
                        onValueChange = { latText = it },
                        label = { Text("Latitude") },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            focusedBorderColor = MintPrimary,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = lngText,
                        onValueChange = { lngText = it },
                        label = { Text("Longitude") },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            focusedBorderColor = MintPrimary,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
                androidx.compose.material3.OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Emergency Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = MintPrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                androidx.compose.material3.OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (e.g. Generators, Medical Staff)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedBorderColor = MintPrimary,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val lat = latText.toDoubleOrNull() ?: currentLat
                    val lng = lngText.toDoubleOrNull() ?: currentLng
                    if (name.isNotBlank()) {
                        onAdd(name, lat, lng, category, capacity, phone, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary, contentColor = Color.White)
            ) { Text("SAVE") }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) { Text("CANCEL", color = TextSecondary) }
        }
    )
}
