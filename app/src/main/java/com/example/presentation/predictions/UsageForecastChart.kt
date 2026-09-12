package com.example.presentation.predictions

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JeevanBatteryAmber
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanFoodYellow
import com.example.ui.theme.JeevanFuelOrange
import com.example.ui.theme.JeevanTextMuted
import com.example.ui.theme.JeevanWaterBlue

@Composable
fun UsageForecastChartCard(
    modifier: Modifier = Modifier
) {
    val enabledLines = remember {
        mutableStateMapOf(
            "Water" to true,
            "Food" to true,
            "Battery" to true,
            "Fuel" to true
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(JeevanCard)
            .border(1.dp, JeevanCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            // Header with range selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Usage Forecast",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1B2735))
                        .border(1.dp, Color(0xFF283A4E), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Next 7 days ▾",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height

                    val leftPadding = 38.dp.toPx()
                    val rightPadding = 16.dp.toPx()
                    val topPadding = 12.dp.toPx()
                    val bottomPadding = 24.dp.toPx()

                    val chartWidth = w - leftPadding - rightPadding
                    val chartHeight = h - topPadding - bottomPadding

                    // Grid Horizontal Lines
                    val gridY100 = topPadding
                    val gridY50 = topPadding + chartHeight * 0.5f
                    val gridY0 = topPadding + chartHeight

                    val gridStroke = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )

                    drawLine(
                        color = Color(0xFF1E2E3E),
                        start = Offset(leftPadding, gridY100),
                        end = Offset(w - rightPadding, gridY100),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF1E2E3E),
                        start = Offset(leftPadding, gridY50),
                        end = Offset(w - rightPadding, gridY50),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF2A3D52),
                        start = Offset(leftPadding, gridY0),
                        end = Offset(w - rightPadding, gridY0),
                        strokeWidth = 1.dp.toPx()
                    )

                    // Critical Threshold vertical dashed line at ~ 31 hours (around day 1.3)
                    val criticalX = leftPadding + chartWidth * 0.22f
                    drawLine(
                        color = Color(0xFFEF4444),
                        start = Offset(criticalX, topPadding),
                        end = Offset(criticalX, gridY0),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                    )

                    // Helper function to plot normalized values [0..1]
                    fun drawSeries(values: List<Float>, color: Color) {
                        if (values.size < 2) return
                        val path = Path()
                        val stepX = chartWidth / (values.size - 1)

                        values.forEachIndexed { i, v ->
                            val x = leftPadding + i * stepX
                            val y = topPadding + chartHeight * (1f - v.coerceIn(0f, 1f))
                            if (i == 0) path.moveTo(x, y)
                            else {
                                val prevX = leftPadding + (i - 1) * stepX
                                val prevY = topPadding + chartHeight * (1f - values[i - 1].coerceIn(0f, 1f))
                                val cX = (prevX + x) / 2f
                                path.cubicTo(cX, prevY, cX, y, x, y)
                            }
                        }

                        drawPath(
                            path = path,
                            color = color,
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                    }

                    // Series data points: Now, 1d, 2d, 3d, 5d, 7d
                    if (enabledLines["Water"] == true) {
                        drawSeries(listOf(0.72f, 0.58f, 0.45f, 0.28f, 0.10f, 0.02f), JeevanWaterBlue)
                    }
                    if (enabledLines["Food"] == true) {
                        drawSeries(listOf(0.68f, 0.59f, 0.51f, 0.40f, 0.25f, 0.12f), Color(0xFF22C55E))
                    }
                    if (enabledLines["Battery"] == true) {
                        drawSeries(listOf(0.45f, 0.35f, 0.22f, 0.12f, 0.04f, 0.0f), JeevanFoodYellow)
                    }
                    if (enabledLines["Fuel"] == true) {
                        drawSeries(listOf(0.63f, 0.55f, 0.48f, 0.39f, 0.28f, 0.18f), JeevanFuelOrange)
                    }
                }

                // Y-Axis labels (100%, 50%, 0%)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("100%", color = JeevanTextMuted, fontSize = 9.sp)
                    Spacer(modifier = Modifier.height(48.dp))
                    Text("50%", color = JeevanTextMuted, fontSize = 9.sp)
                    Spacer(modifier = Modifier.height(48.dp))
                    Text("0%", color = JeevanTextMuted, fontSize = 9.sp)
                }

                // Critical pill badge over the critical line
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 20.dp, top = 20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFDC2626))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Critical",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // X-Axis labels (Now, 1d, 2d, 3d, 5d, 7d)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = 38.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Now", "1d", "2d", "3d", "5d", "7d").forEach { label ->
                        Text(
                            text = label,
                            color = JeevanTextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Toggles below chart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val filters = listOf(
                    Triple("Water", JeevanWaterBlue, enabledLines["Water"] == true),
                    Triple("Food", Color(0xFF22C55E), enabledLines["Food"] == true),
                    Triple("Battery", JeevanFoodYellow, enabledLines["Battery"] == true),
                    Triple("Fuel", JeevanFuelOrange, enabledLines["Fuel"] == true)
                )

                filters.forEach { (name, color, isEnabled) ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { enabledLines[name] = !isEnabled }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isEnabled) color else Color.Transparent)
                                .border(1.dp, color, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = name,
                            color = if (isEnabled) Color.White else JeevanTextMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isEnabled) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
