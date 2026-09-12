package com.example.presentation.map

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanTextMuted

@Composable
fun OfflineMapScreen(
    viewModel: JeevanSetuViewModel,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    var showLayersMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = JeevanBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("offline_map_screen")
        ) {
            // Tactical Dark Map Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Base Tactical Dark Canvas
                drawRect(color = Color(0xFF0C141C))

                // Cartographic Grid Lines
                val gridStep = 40.dp.toPx()
                var x = 0f
                while (x < w) {
                    drawLine(
                        color = Color(0xFF131F2A),
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = 1.dp.toPx()
                    )
                    x += gridStep
                }
                var y = 0f
                while (y < h) {
                    drawLine(
                        color = Color(0xFF131F2A),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.dp.toPx()
                    )
                    y += gridStep
                }

                // Dark Blue River / Waterbody Path
                val riverPath = Path().apply {
                    moveTo(w * 0.1f, 0f)
                    cubicTo(w * 0.35f, h * 0.25f, w * 0.25f, h * 0.6f, w * 0.45f, h)
                    lineTo(w * 0.55f, h)
                    cubicTo(w * 0.35f, h * 0.6f, w * 0.45f, h * 0.25f, w * 0.2f, 0f)
                    close()
                }
                drawPath(path = riverPath, color = Color(0xFF0F2639), style = Fill)
                drawPath(path = riverPath, color = Color(0xFF1B3B55), style = Stroke(width = 1.5.dp.toPx()))

                // Secondary Stream / Water Canal
                val canalPath = Path().apply {
                    moveTo(w * 0.35f, h * 0.38f)
                    cubicTo(w * 0.6f, h * 0.35f, w * 0.75f, h * 0.48f, w, h * 0.45f)
                }
                drawPath(path = canalPath, color = Color(0xFF143048), style = Stroke(width = 4.dp.toPx()))

                // Tactical Road Networks
                val roadLines = listOf(
                    // Major Avenue 1 (Vertical)
                    Pair(Offset(w * 0.62f, 0f), Offset(w * 0.62f, h)),
                    // Major Avenue 2 (Diagonal)
                    Pair(Offset(0f, h * 0.3f), Offset(w, h * 0.75f)),
                    // Ring connector
                    Pair(Offset(w * 0.1f, h * 0.65f), Offset(w * 0.9f, h * 0.65f)),
                    // Secondary streets
                    Pair(Offset(w * 0.3f, h * 0.15f), Offset(w * 0.85f, h * 0.15f)),
                    Pair(Offset(w * 0.4f, h * 0.5f), Offset(w * 0.95f, h * 0.5f))
                )
                roadLines.forEach { (start, end) ->
                    drawLine(
                        color = Color(0xFF203244),
                        start = start,
                        end = end,
                        strokeWidth = 3.dp.toPx()
                    )
                }

                // Flooded Area (Unsafe) Hazard Polygon (Red translucent)
                val floodPolygon = Path().apply {
                    moveTo(w * 0.62f, h * 0.52f)
                    lineTo(w * 0.88f, h * 0.55f)
                    lineTo(w * 0.84f, h * 0.72f)
                    lineTo(w * 0.64f, h * 0.70f)
                    close()
                }
                drawPath(
                    path = floodPolygon,
                    color = Color(0xFFEF4444).copy(alpha = 0.22f),
                    style = Fill
                )
                drawPath(
                    path = floodPolygon,
                    color = Color(0xFFEF4444).copy(alpha = 0.6f),
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                    )
                )

                // Safe Route Line (Bright Neon Emerald Line from User to Shelter)
                val userPos = Offset(w * 0.66f, h * 0.53f)
                val shelterPos = Offset(w * 0.62f, h * 0.68f)

                val safeRoute = Path().apply {
                    moveTo(userPos.x, userPos.y)
                    lineTo(w * 0.62f, h * 0.55f)
                    lineTo(w * 0.62f, h * 0.68f)
                }
                // Glow stroke
                drawPath(
                    path = safeRoute,
                    color = Color(0xFF22C55E).copy(alpha = 0.35f),
                    style = Stroke(width = 8.dp.toPx())
                )
                // Solid green core
                drawPath(
                    path = safeRoute,
                    color = Color(0xFF22C55E),
                    style = Stroke(width = 3.dp.toPx())
                )

                // Resource & Infrastructure Pins on Map
                // 1. Shelter Pins (Green circles with house)
                val shelters = listOf(
                    Offset(w * 0.62f, h * 0.68f), // Nearest shelter
                    Offset(w * 0.25f, h * 0.48f)
                )
                shelters.forEach { pos ->
                    drawCircle(color = Color(0xFF10B981).copy(alpha = 0.3f), radius = 14.dp.toPx(), center = pos)
                    drawCircle(color = Color(0xFF10B981), radius = 8.dp.toPx(), center = pos)
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = pos)
                }

                // 2. Hospital Pins (Red Cross points)
                val hospitals = listOf(
                    Offset(w * 0.82f, h * 0.44f)
                )
                hospitals.forEach { pos ->
                    drawCircle(color = Color(0xFFEF4444).copy(alpha = 0.3f), radius = 14.dp.toPx(), center = pos)
                    drawCircle(color = Color(0xFFEF4444), radius = 8.dp.toPx(), center = pos)
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = pos)
                }

                // 3. Water Source Pins (Blue water drop)
                val waterSources = listOf(
                    Offset(w * 0.36f, h * 0.44f),
                    Offset(w * 0.72f, h * 0.33f)
                )
                waterSources.forEach { pos ->
                    drawCircle(color = Color(0xFF38BDF8).copy(alpha = 0.3f), radius = 14.dp.toPx(), center = pos)
                    drawCircle(color = Color(0xFF38BDF8), radius = 8.dp.toPx(), center = pos)
                    drawCircle(color = Color.White, radius = 3.dp.toPx(), center = pos)
                }

                // 4. Community Point (Orange)
                val communityPoints = listOf(
                    Offset(w * 0.32f, h * 0.54f)
                )
                communityPoints.forEach { pos ->
                    drawCircle(color = Color(0xFFF97316).copy(alpha = 0.3f), radius = 14.dp.toPx(), center = pos)
                    drawCircle(color = Color(0xFFF97316), radius = 8.dp.toPx(), center = pos)
                }

                // 5. User Position ("● You" Blue pulsating GPS marker)
                drawCircle(color = Color(0xFF38BDF8).copy(alpha = 0.25f), radius = 18.dp.toPx(), center = userPos)
                drawCircle(color = Color(0xFF38BDF8), radius = 9.dp.toPx(), center = userPos)
                drawCircle(color = Color.White, radius = 4.dp.toPx(), center = userPos)
            }

            // Top Bar: [ <- Offline Map ] + [ Layers Icon ] + [ Target Icon ]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = "Offline Map",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showLayersMenu = !showLayersMenu },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF15222E))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Layers",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { viewModel.refreshLocation() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF15222E))
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Floating Tactical Map Legend (Matching Phone 4)
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xEE111C26))
                    .border(1.dp, Color(0xFF1F3244), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    LegendItem(dotColor = Color(0xFF38BDF8), label = "You")
                    LegendItem(dotColor = Color(0xFF22C55E), label = "Shelter")
                    LegendItem(dotColor = Color(0xFFEF4444), label = "Hospital")
                    LegendItem(dotColor = Color(0xFF0EA5E9), label = "Water Source")
                    LegendItem(dotColor = Color(0xFFF97316), label = "Community Point")
                    LegendLine(lineColor = Color(0xFF22C55E), label = "Safe Route")
                    LegendLine(lineColor = Color(0xFFEF4444), label = "Restricted Area", isDashed = true)
                }
            }

            // Label for Flooded Area (Unsafe)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 40.dp, bottom = 40.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Flooded Area",
                        color = Color(0xFFF87171),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(Unsafe)",
                        color = Color(0xFFFCA5A5),
                        fontSize = 10.sp
                    )
                }
            }

            // Map Scale Indicator (2 km)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 120.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(2.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "2 km",
                        color = Color(0xFFCBD5E1),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Tactical Floating Action Buttons (Recenter / Compass)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = { /* Compass orient */ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF152331))
                        .border(1.dp, Color(0xFF23374D), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = "Compass",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { viewModel.refreshLocation() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF152331))
                        .border(1.dp, Color(0xFF23374D), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Target",
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Nearest Shelter Floating Card at Bottom (Matching Phone 4)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF121E2A))
                    .border(1.dp, Color(0xFF223547), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
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
                                    .background(Color(0xFF133626)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Shelter",
                                    tint = JeevanBrandGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Nearest Shelter",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "1.2 km",
                                        color = Color(0xFF86EFAC),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Community Hall, Sector 22",
                                    color = JeevanTextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = JeevanTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Green Action Button: Navigate (Offline)
                    Button(
                        onClick = {
                            val uri = Uri.parse("geo:28.6139,77.2090?q=Community+Hall+Sector+22")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            try {
                                context.startActivity(mapIntent)
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NearMe,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Navigate (Offline)",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
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
            color = Color(0xFFCBD5E1),
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
            color = Color(0xFFCBD5E1),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
