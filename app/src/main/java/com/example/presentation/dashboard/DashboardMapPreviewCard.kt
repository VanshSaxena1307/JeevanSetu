package com.example.presentation.dashboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AppLanguage
import com.example.domain.model.AppStrings
import com.example.domain.model.LocalizationData
import com.example.ui.theme.AppBackground
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.utils.DeviceLocation

@Composable
fun DashboardMapPreviewCard(
    currentLocation: DeviceLocation,
    displayCity: String,
    onClickMap: () -> Unit,
    strings: AppStrings = LocalizationData.getStrings(AppLanguage.ENGLISH),
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radarPulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .testTag("dashboard_map_preview")
            .clickable(onClick = onClickMap),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Stylized vector topographic canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Subtle tactical gridlines
                val gridColor = BorderSubtle
                for (x in 0..w.toInt() step 60) {
                    drawLine(gridColor, Offset(x.toFloat(), 0f), Offset(x.toFloat(), h), strokeWidth = 1f)
                }
                for (y in 0..h.toInt() step 45) {
                    drawLine(gridColor, Offset(0f, y.toFloat()), Offset(w, y.toFloat()), strokeWidth = 1f)
                }

                // Topographical contour lines
                val contourColor = MintLight
                val path1 = Path().apply {
                    moveTo(0f, h * 0.7f)
                    cubicTo(w * 0.25f, h * 0.45f, w * 0.45f, h * 0.85f, w * 0.75f, h * 0.5f)
                    cubicTo(w * 0.85f, h * 0.4f, w * 0.95f, h * 0.6f, w, h * 0.55f)
                }
                drawPath(path1, contourColor, style = Stroke(width = 1.5f))

                val path2 = Path().apply {
                    moveTo(0f, h * 0.4f)
                    cubicTo(w * 0.3f, h * 0.25f, w * 0.6f, h * 0.5f, w, h * 0.35f)
                }
                drawPath(path2, contourColor.copy(alpha = 0.7f), style = Stroke(width = 1.2f))

                val path3 = Path().apply {
                    moveTo(w * 0.15f, 0f)
                    cubicTo(w * 0.35f, h * 0.3f, w * 0.55f, h * 0.15f, w * 0.8f, 0f)
                }
                drawPath(path3, contourColor.copy(alpha = 0.5f), style = Stroke(width = 1.2f))

                // Region outlines / landmass representation
                val coastPath = Path().apply {
                    moveTo(w * 0.82f, 0f)
                    cubicTo(w * 0.75f, h * 0.4f, w * 0.88f, h * 0.7f, w * 0.72f, h)
                    lineTo(w, h)
                    lineTo(w, 0f)
                    close()
                }
                drawPath(coastPath, MintLight.copy(alpha = 0.6f))

                // Radar ping ring around center marker
                val centerPt = Offset(w * 0.48f, h * 0.52f)
                drawCircle(
                    color = Color(0xFFF59E0B).copy(alpha = 0.15f * (1f - radarPulse)),
                    radius = 50f * radarPulse + 16f,
                    center = centerPt
                )
                drawCircle(
                    color = Color(0xFFF59E0B).copy(alpha = 0.25f),
                    radius = 22f,
                    center = centerPt,
                    style = Stroke(width = 2f)
                )
            }

            // Region Labels
            Box(modifier = Modifier.padding(14.dp).align(Alignment.TopStart)) {
                Text(
                    text = strings.sectorName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }

            Box(modifier = Modifier.padding(14.dp).align(Alignment.TopEnd)) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(MintPrimary)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = strings.offlineBadge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            // Center Tactical Pin Marker (Warning Triangle in Amber Pill)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEF3C7))
                    .border(1.5.dp, Color(0xFFF59E0B), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = displayCity.ifEmpty { strings.locationTitle },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
            }

            // Bottom action strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(SurfaceWhite.copy(alpha = 0.95f))
                    .border(1.dp, BorderSubtle)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = MintPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.tacticalVectorLayer,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = strings.openMap,
                        fontSize = 11.sp,
                        color = MintPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MintPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
