package com.example.presentation.map

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.db.MapRegionEntity
import com.example.data.local.db.SafeLocationEntity
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
import com.example.utils.GeoLocationUtils
import com.example.utils.MapStorageExporter
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OfflineMapScreen(
    viewModel: JeevanSetuViewModel,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Interactive Map, 1: Offline Regions
    var showLayersDialog by remember { mutableStateOf(false) }
    var regionToDelete by remember { mutableStateOf<MapRegionEntity?>(null) }
    var showOfflineDownloadNotice by remember { mutableStateOf(false) }

    // Layer toggles
    var showSheltersLayer by remember { mutableStateOf(true) }
    var showHospitalsLayer by remember { mutableStateOf(true) }
    var showHazardLayer by remember { mutableStateOf(true) }
    var showRouteLayer by remember { mutableStateOf(true) }

    // Live State from ViewModel
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val safeLocations by viewModel.safeLocations.collectAsStateWithLifecycle()
    val mapRegions by viewModel.mapRegions.collectAsStateWithLifecycle()
    val isDownloadingArea by viewModel.isDownloadingAreaMap.collectAsStateWithLifecycle()
    val storageUsageBytes by viewModel.offlineStorageUsageBytes.collectAsStateWithLifecycle()
    val availableStorageBytes by viewModel.availableStorageBytes.collectAsStateWithLifecycle()
    val downloadError by viewModel.downloadError.collectAsStateWithLifecycle()

    var selectedLocation by remember { mutableStateOf<SafeLocationEntity?>(null) }
    var centerTargetGeoPoint by remember { mutableStateOf<GeoPoint?>(null) }

    // Nearest safe location calculation
    val nearestLocation = remember(safeLocations, currentLocation) {
        safeLocations.minByOrNull {
            GeoLocationUtils.calculateDistanceKm(
                currentLocation.latitude,
                currentLocation.longitude,
                it.latitude,
                it.longitude
            )
        }
    }

    val activeInspectorLocation = selectedLocation ?: nearestLocation

    LaunchedEffect(Unit) {
        viewModel.updateStorageMetrics()
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite)
                    .border(width = 0.5.dp, color = BorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (onBack != null) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = "Offline Maps",
                            color = TextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Online / Offline Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isOnline) Color(0xFFD1FAE5) else MintLight)
                                .border(
                                    width = 1.dp,
                                    color = if (isOnline) SafetyGreen.copy(alpha = 0.4f) else MintDeep.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 9.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (isOnline) SafetyGreen else MintDeep)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (isOnline) "Live Map" else "Offline Map",
                                    color = if (isOnline) Color(0xFF065F46) else MintDeep,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { showLayersDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AppBackground)
                                .border(1.dp, BorderSubtle, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Layers",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Segmented Tabs: [ Interactive Map | Offline Regions ]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf("Interactive Map", "Offline Regions")
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MintLight else Color.Transparent)
                                .clickable { selectedTab = index }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) MintDeep else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("offline_map_screen")
        ) {
            when (selectedTab) {
                0 -> {
                    // TAB 0: Interactive Map View
                    InteractiveMapView(
                        isOnline = isOnline,
                        currentLocation = currentLocation,
                        safeLocations = safeLocations,
                        nearestLocation = activeInspectorLocation,
                        showShelters = showSheltersLayer,
                        showHospitals = showHospitalsLayer,
                        showHazards = showHazardLayer,
                        showRoute = showRouteLayer,
                        centerTarget = centerTargetGeoPoint,
                        onSelectLocation = { selectedLocation = it },
                        onRecenter = {
                            viewModel.refreshLocation()
                            centerTargetGeoPoint = GeoPoint(currentLocation.latitude, currentLocation.longitude)
                        }
                    )
                }

                1 -> {
                    // TAB 1: Offline Region Management
                    OfflineRegionManagementView(
                        isOnline = isOnline,
                        mapRegions = mapRegions,
                        storageUsageBytes = storageUsageBytes,
                        availableStorageBytes = availableStorageBytes,
                        isDownloadingArea = isDownloadingArea,
                        downloadError = downloadError,
                        onDownloadRegion = { region ->
                            if (!isOnline) {
                                showOfflineDownloadNotice = true
                            } else {
                                viewModel.downloadMapRegion(region)
                            }
                        },
                        onDeleteRegion = { region ->
                            regionToDelete = region
                        },
                        onViewOnMap = { region ->
                            centerTargetGeoPoint = GeoPoint(region.centerLat, region.centerLng)
                            selectedTab = 0
                        },
                        onDownloadCurrentSector = {
                            if (!isOnline) {
                                showOfflineDownloadNotice = true
                            } else {
                                viewModel.detectAndDownloadUserAreaMap()
                            }
                        },
                        onOpenDownloads = {
                            MapStorageExporter.openDownloadsFolder(context)
                        },
                        onDismissError = {
                            viewModel.clearDownloadError()
                        }
                    )
                }
            }

            // Error notice banner if download error occurs
            if (downloadError != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEF2F2))
                        .border(1.dp, EmergencyRed.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = null,
                                tint = EmergencyRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = downloadError ?: "Error downloading map data",
                                color = Color(0xFF991B1B),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        IconButton(
                            onClick = { viewModel.clearDownloadError() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Dismiss",
                                tint = EmergencyRed
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog: Delete Region Confirmation
    if (regionToDelete != null) {
        val reg = regionToDelete!!
        AlertDialog(
            onDismissRequest = { regionToDelete = null },
            title = { Text("Delete Offline Map", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "Are you sure you want to remove the cached offline map data for ${reg.regionName}? You can download it again when connected to the internet.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMapRegion(reg)
                        regionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { regionToDelete = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceWhite
        )
    }

    // Dialog: Offline Download Warning
    if (showOfflineDownloadNotice) {
        AlertDialog(
            onDismissRequest = { showOfflineDownloadNotice = false },
            title = { Text("Internet Connection Required", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "To download new offline map sectors, please connect to Wi-Fi or mobile data. All previously downloaded regions remain fully accessible offline without internet.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showOfflineDownloadNotice = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary)
                ) {
                    Text("Understood", color = Color.White)
                }
            },
            containerColor = SurfaceWhite
        )
    }

    // Dialog: Layer Controls
    if (showLayersDialog) {
        AlertDialog(
            onDismissRequest = { showLayersDialog = false },
            title = { Text("Map Layer Visibility", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LayerToggleRow(
                        title = "Emergency Shelters",
                        subtitle = "Safe refuges & high-ground centers",
                        checked = showSheltersLayer,
                        onCheckedChange = { showSheltersLayer = it }
                    )
                    LayerToggleRow(
                        title = "Hospitals & Trauma Centers",
                        subtitle = "Medical triage facilities",
                        checked = showHospitalsLayer,
                        onCheckedChange = { showHospitalsLayer = it }
                    )
                    LayerToggleRow(
                        title = "Hazard & Flood Zones",
                        subtitle = "Submerged & dangerous sectors",
                        checked = showHazardLayer,
                        onCheckedChange = { showHazardLayer = it }
                    )
                    LayerToggleRow(
                        title = "Evacuation Routes",
                        subtitle = "Tactical safe passage line",
                        checked = showRouteLayer,
                        onCheckedChange = { showRouteLayer = it }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showLayersDialog = false }) {
                    Text("Done", color = MintDeep, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceWhite
        )
    }
}

@Composable
private fun InteractiveMapView(
    isOnline: Boolean,
    currentLocation: com.example.utils.DeviceLocation,
    safeLocations: List<SafeLocationEntity>,
    nearestLocation: SafeLocationEntity?,
    showShelters: Boolean,
    showHospitals: Boolean,
    showHazards: Boolean,
    showRoute: Boolean,
    centerTarget: GeoPoint?,
    onSelectLocation: (SafeLocationEntity) -> Unit,
    onRecenter: () -> Unit
) {
    val context = LocalContext.current
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    val initialCenter = remember {
        centerTarget ?: GeoPoint(currentLocation.latitude, currentLocation.longitude)
    }

    LaunchedEffect(centerTarget) {
        if (centerTarget != null && mapViewInstance != null) {
            mapViewInstance?.controller?.animateTo(centerTarget)
            mapViewInstance?.controller?.setZoom(14.0)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    minZoomLevel = 10.0
                    maxZoomLevel = 18.0
                    isTilesScaledToDpi = true
                    (tileProvider as? MapTileProviderBasic)?.setOfflineFirst(true)
                    controller.setZoom(13.0)
                    controller.setCenter(initialCenter)
                    setUseDataConnection(isOnline)
                    mapViewInstance = this
                }
            },
            update = { mapView ->
                mapView.setUseDataConnection(isOnline)
                mapView.overlays.clear()

                // OSM Mandatory Copyright Overlay (ODbL / CC-BY-SA compliance)
                val copyrightOverlay = CopyrightOverlay(context).apply {
                    setTextSize(10)
                    setAlignBottom(true)
                    setAlignRight(true)
                    setOffset(14, 14)
                }
                mapView.overlays.add(copyrightOverlay)

                // 1. User Position Marker
                val userMarker = Marker(mapView).apply {
                    position = GeoPoint(currentLocation.latitude, currentLocation.longitude)
                    title = "Your Location"
                    snippet = "GPS Accuracy: ${currentLocation.accuracyMeters.toInt()}m"
                    icon = MapMarkerHelper.createUserMarker(context)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
                mapView.overlays.add(userMarker)

                // 2. Safe Shelters and Hospitals
                safeLocations.forEach { loc ->
                    val isHospital = loc.category.equals("Hospital", ignoreCase = true)
                    if ((isHospital && showHospitals) || (!isHospital && showShelters)) {
                        val marker = Marker(mapView).apply {
                            position = GeoPoint(loc.latitude, loc.longitude)
                            title = loc.name
                            snippet = "${loc.category} • Capacity: ${loc.capacity}"
                            icon = if (isHospital) {
                                MapMarkerHelper.createLocationPin(context, 0xFFDC2626.toInt(), "+")
                            } else {
                                MapMarkerHelper.createLocationPin(context, 0xFF168F78.toInt(), "S")
                            }
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            setOnMarkerClickListener { _, _ ->
                                onSelectLocation(loc)
                                true
                            }
                        }
                        mapView.overlays.add(marker)
                    }
                }

                // 3. Flood Hazard Polygon Overlay
                if (showHazards) {
                    val hazardPoly = Polygon(mapView).apply {
                        val baseLat = currentLocation.latitude + 0.007
                        val baseLng = currentLocation.longitude + 0.010
                        val pts = listOf(
                            GeoPoint(baseLat - 0.005, baseLng - 0.008),
                            GeoPoint(baseLat + 0.006, baseLng - 0.007),
                            GeoPoint(baseLat + 0.008, baseLng + 0.009),
                            GeoPoint(baseLat - 0.004, baseLng + 0.008)
                        )
                        points = pts
                        fillPaint.color = 0x22DC2626.toInt() // Soft red tint
                        outlinePaint.color = 0xFFDC2626.toInt()
                        outlinePaint.strokeWidth = 3f
                        title = "Flooded Hazard Sector (Restricted)"
                    }
                    mapView.overlays.add(hazardPoly)
                }

                // 4. Safe Evacuation Route Polyline
                if (showRoute && nearestLocation != null) {
                    val routePoly = Polyline(mapView).apply {
                        val routePts = listOf(
                            GeoPoint(currentLocation.latitude, currentLocation.longitude),
                            GeoPoint((currentLocation.latitude + nearestLocation.latitude) / 2.0, currentLocation.longitude + 0.002),
                            GeoPoint(nearestLocation.latitude, nearestLocation.longitude)
                        )
                        setPoints(routePts)
                        outlinePaint.color = 0xFF168F78.toInt() // Mint Deep
                        outlinePaint.strokeWidth = 7f
                    }
                    mapView.overlays.add(routePoly)
                }

                mapView.invalidate()
            },
            modifier = Modifier.fillMaxSize()
        )

        // Lifecycle for MapView
        DisposableEffect(mapViewInstance) {
            mapViewInstance?.onResume()
            onDispose {
                mapViewInstance?.onPause()
                mapViewInstance?.onDetach()
            }
        }

        // Floating Tactical Map Controls (Right Side)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Zoom In (+)
            IconButton(
                onClick = { mapViewInstance?.controller?.zoomIn() },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfaceWhite)
                    .border(1.dp, BorderSubtle, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = TextPrimary)
            }

            // Zoom Out (-)
            IconButton(
                onClick = { mapViewInstance?.controller?.zoomOut() },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfaceWhite)
                    .border(1.dp, BorderSubtle, CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = TextPrimary)
            }

            // Recenter ("My Location")
            IconButton(
                onClick = onRecenter,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfaceWhite)
                    .border(1.dp, BorderSubtle, CircleShape)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location", tint = MintPrimary)
            }
        }

        // Floating Map Legend
        Box(
            modifier = Modifier
                .padding(start = 14.dp, top = 14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceWhite.copy(alpha = 0.94f))
                .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                LegendItem(dotColor = MintPrimary, label = "You (GPS)")
                LegendItem(dotColor = MintDeep, label = "Safe Shelter")
                LegendItem(dotColor = EmergencyRed, label = "Hospital")
                LegendLine(lineColor = MintDeep, label = "Evacuation Route")
                LegendLine(lineColor = EmergencyRed, label = "Flooded Hazard Zone", isDashed = true)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "© OpenStreetMap contributors",
                    color = TextSecondary.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Nearest / Selected Safe Location Bottom Card
        if (nearestLocation != null) {
            val distKm = GeoLocationUtils.calculateDistanceKm(
                currentLocation.latitude,
                currentLocation.longitude,
                nearestLocation.latitude,
                nearestLocation.longitude
            )

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (nearestLocation.category.equals("Hospital", ignoreCase = true)) {
                                            Color(0xFFFEE2E2)
                                        } else {
                                            MintLight
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (nearestLocation.category.equals("Hospital", ignoreCase = true)) {
                                        Icons.Default.LocalHospital
                                    } else {
                                        Icons.Default.Home
                                    },
                                    contentDescription = null,
                                    tint = if (nearestLocation.category.equals("Hospital", ignoreCase = true)) {
                                        EmergencyRed
                                    } else {
                                        MintDeep
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = nearestLocation.name,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "${nearestLocation.category} • Capacity: ${nearestLocation.capacity}",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MintLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.1f km", distKm),
                                color = MintDeep,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Navigate Button
                        Button(
                            onClick = {
                                val uri = Uri.parse("geo:${nearestLocation.latitude},${nearestLocation.longitude}?q=${Uri.encode(nearestLocation.name)}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                try {
                                    context.startActivity(mapIntent)
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Navigate (Offline)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        // Call Shelter Button
                        if (nearestLocation.contactPhone.isNotEmpty()) {
                            Button(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${nearestLocation.contactPhone}"))
                                    try {
                                        context.startActivity(dialIntent)
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier
                                    .weight(0.7f)
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AppBackground),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(15.dp), tint = TextPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 12.sp, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfflineRegionManagementView(
    isOnline: Boolean,
    mapRegions: List<MapRegionEntity>,
    storageUsageBytes: Long,
    availableStorageBytes: Long,
    isDownloadingArea: Boolean,
    downloadError: String?,
    onDownloadRegion: (MapRegionEntity) -> Unit,
    onDeleteRegion: (MapRegionEntity) -> Unit,
    onViewOnMap: (MapRegionEntity) -> Unit,
    onDownloadCurrentSector: () -> Unit,
    onOpenDownloads: () -> Unit,
    onDismissError: () -> Unit
) {
    val downloaded = mapRegions.filter { it.isDownloaded }
    val available = mapRegions.filter { !it.isDownloaded }

    val formattedUsedMb = String.format(Locale.US, "%.1f MB", storageUsageBytes.toDouble() / (1024.0 * 1024.0))
    val formattedFreeGb = String.format(Locale.US, "%.1f GB", availableStorageBytes.toDouble() / (1024.0 * 1024.0 * 1024.0))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Storage Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Offline Storage Usage",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$formattedUsedMb cached • $formattedFreeGb free on device",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(
                            onClick = onOpenDownloads,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MintLight)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = "Open Downloads",
                                tint = MintDeep,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MintPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Offline Map Engine Ready. Cached sectors work without cellular or Wi-Fi.",
                            color = MintDeep,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 2. Download Current GPS Sector Button
        item {
            Button(
                onClick = onDownloadCurrentSector,
                enabled = !isDownloadingArea,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isDownloadingArea) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Downloading Live GPS Sector...", color = Color.White, fontSize = 13.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Current GPS Area (25 km Radius)",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 3. Downloaded Regions Section
        item {
            Text(
                text = "Downloaded Offline Sectors (${downloaded.size})",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (downloaded.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No offline sectors downloaded yet. Tap 'Download' below to save a map sector.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(downloaded, key = { it.id }) { region ->
                DownloadedRegionCard(
                    region = region,
                    onViewOnMap = { onViewOnMap(region) },
                    onDelete = { onDeleteRegion(region) }
                )
            }
        }

        // 4. Available Regions to Download Section
        if (available.isNotEmpty()) {
            item {
                Text(
                    text = "Available Emergency Sectors (${available.size})",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(available, key = { it.id }) { region ->
                AvailableRegionCard(
                    region = region,
                    onDownload = { onDownloadRegion(region) }
                )
            }
        }

        // 5. OpenStreetMap Licensing & Attribution Compliance Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = MintDeep,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Map Data & Licensing Compliance",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Map data © OpenStreetMap contributors. Cartography and map tiles are licensed under Creative Commons Attribution-ShareAlike 2.0 (CC BY-SA) and the Open Database License (ODbL).",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Offline tile packages are pre-cached and downloaded strictly for emergency civil defense, disaster evacuation, and humanitarian life-safety response. Learn more at openstreetmap.org/copyright.",
                        color = TextSecondary.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadedRegionCard(
    region: MapRegionEntity,
    onViewOnMap: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = region.regionName,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Center: ${String.format(Locale.US, "%.3f, %.3f", region.centerLat, region.centerLng)} • Radius: ${region.radiusKm.toInt()} km",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MintLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Offline Ready",
                        color = MintDeep,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dateStr = if (region.downloadDate > 0) {
                SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(region.downloadDate))
            } else {
                "Preloaded Sector"
            }

            Text(
                text = "Size: ${region.sizeMb} MB • ~${region.tileCount} tiles • Downloaded: $dateStr",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = EmergencyRed)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onViewOnMap,
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("View on Map", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun AvailableRegionCard(
    region: MapRegionEntity,
    onDownload: () -> Unit
) {
    val isDownloading = region.downloadProgress in 1..99

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = region.regionName,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Approx. ${region.sizeMb} MB • ~${region.tileCount} tactical tiles",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                if (!isDownloading) {
                    Button(
                        onClick = onDownload,
                        colors = ButtonDefaults.buttonColors(containerColor = MintLight),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(15.dp), tint = MintDeep)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Download", fontSize = 12.sp, color = MintDeep, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isDownloading) {
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { region.downloadProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MintPrimary,
                    trackColor = MintLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Downloading offline tiles...", color = TextSecondary, fontSize = 11.sp)
                    Text("${region.downloadProgress}%", color = MintDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun LayerToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MintPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BorderSubtle
            )
        )
    }
}

@Composable
private fun LegendItem(
    dotColor: Color,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LegendLine(
    lineColor: Color,
    label: String,
    isDashed: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(2.dp)
                .background(lineColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
