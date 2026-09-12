package com.example.presentation.dashboard

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AlertCardType
import com.example.domain.model.AlertSeverity
import com.example.domain.model.AppLanguage
import com.example.domain.model.AppStrings
import com.example.domain.model.DisasterAlert
import com.example.domain.model.LocalizationData
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.MintPrimary
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.utils.EmergencyAudioAnnouncer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisasterAlertCard(
    alert: DisasterAlert,
    audioAnnouncer: EmergencyAudioAnnouncer,
    language: AppLanguage = AppLanguage.ENGLISH,
    strings: AppStrings = LocalizationData.getStrings(language),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDosDontsSheet by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }

    val isCoral = alert.cardType == AlertCardType.CORAL_CARD
    val isPeach = alert.cardType == AlertCardType.PEACH_CARD

    val cardBg = when (alert.cardType) {
        AlertCardType.WHITE_CARD -> Color(0xFFFFFFFF)
        AlertCardType.PEACH_CARD -> Color(0xFFFFEDD5)
        AlertCardType.CORAL_CARD -> Color(0xFFEF4444)
    }

    val primaryTextColor = when (alert.cardType) {
        AlertCardType.WHITE_CARD -> Color(0xFF0F172A)
        AlertCardType.PEACH_CARD -> Color(0xFF7C2D12)
        AlertCardType.CORAL_CARD -> Color(0xFFFFFFFF)
    }

    val secondaryTextColor = when (alert.cardType) {
        AlertCardType.WHITE_CARD -> Color(0xFF64748B)
        AlertCardType.PEACH_CARD -> Color(0xFF9A3412)
        AlertCardType.CORAL_CARD -> Color(0xFFFEE2E2)
    }

    val borderStroke = when (alert.cardType) {
        AlertCardType.WHITE_CARD -> BorderStroke(1.dp, Color(0xFFE2E8F0))
        AlertCardType.PEACH_CARD -> BorderStroke(1.dp, Color(0xFFFED7AA))
        AlertCardType.CORAL_CARD -> null
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("alert_card_${alert.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCoral) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Title and Intensity Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor
                    ),
                    modifier = Modifier.weight(1f, fill = false)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Intensity Badge
                val badgeBg = when (alert.severity) {
                    AlertSeverity.LOW -> Color(0xFFFEF9C3)
                    AlertSeverity.MODERATE -> Color(0xFFFEF08A)
                    AlertSeverity.HIGH -> if (isCoral) Color.White else Color(0xFFFEE2E2)
                    AlertSeverity.SEVERE -> Color(0xFFF97316)
                }
                val badgeText = when (alert.severity) {
                    AlertSeverity.LOW -> Color(0xFF854D0E)
                    AlertSeverity.MODERATE -> Color(0xFF854D0E)
                    AlertSeverity.HIGH -> if (isCoral) Color(0xFFDC2626) else Color(0xFFDC2626)
                    AlertSeverity.SEVERE -> Color.White
                }

                val localizedSeverity = LocalizationData.getLocalizedSeverityLabel(alert.severity, language)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(badgeBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = localizedSeverity,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Issuer
            Text(
                text = alert.issuedBy,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = secondaryTextColor,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Metadata Row: Location and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = alert.location,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = secondaryTextColor
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = secondaryTextColor,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = alert.timestamp,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = secondaryTextColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions Row: Share, Listen, Dos & Don't
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Share Button
                val buttonBg = if (isCoral) Color.White.copy(alpha = 0.2f) else MintLight
                val buttonContentColor = if (isCoral) Color.White else MintDeep
                val buttonBorder = if (isCoral) BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)) else BorderStroke(1.dp, BorderSubtle)

                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "EMERGENCY ALERT: ${alert.title}")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "[JeevanSetu Alert]\n${alert.title} - ${LocalizationData.getLocalizedSeverityLabel(alert.severity, language)}\n${alert.issuedBy}\nLocation: ${alert.location}\nTime: ${alert.timestamp}\n\nAdvisory: ${alert.description}\n\nStay safe and follow official instructions."
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Emergency Alert"))
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonBg, contentColor = buttonContentColor),
                    border = buttonBorder,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.share, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                // Listen Button (Speech Playback)
                Button(
                    onClick = {
                        if (isSpeaking) {
                            audioAnnouncer.stop()
                            isSpeaking = false
                        } else {
                            isSpeaking = true
                            val speakText = "${alert.title}. ${LocalizationData.getLocalizedSeverityLabel(alert.severity, language)}. ${alert.issuedBy}. ${alert.location}. ${alert.description}. ${alert.dos.firstOrNull() ?: ""}"
                            audioAnnouncer.speak(
                                text = speakText,
                                language = language,
                                onDone = { isSpeaking = false }
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1.15f)
                        .then(if (isSpeaking) Modifier.scale(pulseScale) else Modifier),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSpeaking) Color(0xFF0284C7) else buttonBg,
                        contentColor = if (isSpeaking) Color.White else buttonContentColor
                    ),
                    border = buttonBorder,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSpeaking) strings.playing else strings.listen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Dos & Don't Button
                Button(
                    onClick = { showDosDontsSheet = true },
                    modifier = Modifier.weight(1.25f),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonBg, contentColor = buttonContentColor),
                    border = buttonBorder,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(strings.dosAndDonts, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    // Modal Sheet for Dos & Don'ts
    if (showDosDontsSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showDosDontsSheet = false
                if (isSpeaking) {
                    audioAnnouncer.stop()
                    isSpeaking = false
                }
            },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = alert.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        )
                        Text(
                            text = strings.safetyProtocols,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (isSpeaking) {
                                    audioAnnouncer.stop()
                                    isSpeaking = false
                                } else {
                                    isSpeaking = true
                                    val fullDos = alert.dos.joinToString(". ")
                                    val fullDonts = alert.donts.joinToString(". ")
                                    val textToRead = "${alert.title}. ${strings.dosTitle}: $fullDos. ${strings.dontsTitle}: $fullDonts."
                                    audioAnnouncer.speak(
                                        text = textToRead,
                                        language = language,
                                        onDone = { isSpeaking = false }
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                contentDescription = "Voice Guide",
                                tint = if (isSpeaking) Color(0xFFEF4444) else Color(0xFF0284C7)
                            )
                        }

                        IconButton(onClick = {
                            showDosDontsSheet = false
                            if (isSpeaking) {
                                audioAnnouncer.stop()
                                isSpeaking = false
                            }
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Advisory overview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = alert.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155), lineHeight = 18.sp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // DOs Section
                Text(
                    text = strings.dosTitle,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                alert.dos.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDCFCE7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(12.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1F2937), fontSize = 13.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // DONTs Section
                Text(
                    text = strings.dontsTitle,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF991B1B)
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                alert.donts.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(12.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1F2937), fontSize = 13.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showDosDontsSheet = false
                        if (isSpeaking) {
                            audioAnnouncer.stop()
                            isSpeaking = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MintPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(strings.close, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
