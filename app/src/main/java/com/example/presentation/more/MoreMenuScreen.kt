package com.example.presentation.more

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AppLanguage
import com.example.presentation.components.JeevanSetuBrandHeader
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanTextMuted

@Composable
fun MoreMenuScreen(
    currentLanguage: AppLanguage,
    onNavigateToContacts: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToFirstAid: () -> Unit,
    onNavigateToFamily: () -> Unit,
    onNavigateToCalculator: () -> Unit,
    onNavigateToAssessment: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onOpenLanguageSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JeevanBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("more_menu_screen")
    ) {
        // Header
        JeevanSetuBrandHeader()

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "DISASTER RESILIENCE SUITE",
            color = JeevanTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Language Pill Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF132332))
                .border(1.dp, Color(0xFF1F354D), RoundedCornerShape(14.dp))
                .clickable { onOpenLanguageSheet() }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1B3B59)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "App Language / भाषा",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${currentLanguage.nativeName} (${currentLanguage.englishName})",
                            color = Color(0xFF38BDF8),
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
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Emergency & Survival Tools",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MoreNavigationItem(
                icon = Icons.Default.PhoneInTalk,
                iconColor = Color(0xFFEF4444),
                title = "Emergency Contacts & SOS",
                subtitle = "Direct offline dial to Police, Fire, Ambulance & NDRF",
                onClick = onNavigateToContacts
            )
            MoreNavigationItem(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                iconColor = Color(0xFF38BDF8),
                title = "Disaster Survival Guides",
                subtitle = "Verified offline SOPs for Floods, Earthquakes & Cyclones",
                onClick = onNavigateToGuides
            )
            MoreNavigationItem(
                icon = Icons.Default.LocalHospital,
                iconColor = Color(0xFFF43F5E),
                title = "First Aid Encyclopedia",
                subtitle = "Life-saving emergency instructions with visual steps",
                onClick = onNavigateToFirstAid
            )
            MoreNavigationItem(
                icon = Icons.Default.CrisisAlert,
                iconColor = Color(0xFFF59E0B),
                title = "Risk Assessment Engine",
                subtitle = "Evaluate danger level & evacuation recommendation",
                onClick = onNavigateToAssessment
            )
            MoreNavigationItem(
                icon = Icons.Default.Calculate,
                iconColor = Color(0xFF10B981),
                title = "Survival Days Calculator",
                subtitle = "Calculate water, meal ration & power longevity",
                onClick = onNavigateToCalculator
            )
            MoreNavigationItem(
                icon = Icons.Default.FamilyRestroom,
                iconColor = Color(0xFFA855F7),
                title = "Family Profile & Headcount",
                subtitle = "Manage family members, medical needs & locations",
                onClick = onNavigateToFamily
            )
            MoreNavigationItem(
                icon = Icons.Default.Checklist,
                iconColor = Color(0xFF06B6D4),
                title = "Emergency Go-Bag Checklist",
                subtitle = "Track readiness of essential supplies",
                onClick = onNavigateToChecklist
            )
            MoreNavigationItem(
                icon = Icons.Default.Settings,
                iconColor = JeevanTextMuted,
                title = "System Settings & GPS",
                subtitle = "Sync coordinates, toggle battery mode & cache",
                onClick = onNavigateToSettings
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun MoreNavigationItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(JeevanCard)
            .border(1.dp, JeevanCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1B2735)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = JeevanTextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = JeevanTextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
