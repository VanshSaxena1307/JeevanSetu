package com.example.presentation.navigation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.AppLanguage
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.guidance.ActionableGuidanceScreen
import com.example.presentation.map.OfflineMapScreen
import com.example.presentation.more.MoreMenuScreen
import com.example.presentation.resources.ResourceManagerScreen
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.MintDeep
import com.example.ui.theme.MintLight
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: JeevanSetuViewModel,
    onNavigateToAssessment: () -> Unit,
    onNavigateToEvacuation: () -> Unit,
    onNavigateToCalculator: () -> Unit,
    onNavigateToGuides: () -> Unit,
    onNavigateToFirstAid: () -> Unit,
    onNavigateToSafeLocations: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToFamily: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(MainTab.HOME) }
    var showLanguageSheet by remember { mutableStateOf(false) }

    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = JeevanBg,
        bottomBar = {
            JeevanSetuBottomNav(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.HOME -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToAssessment = onNavigateToAssessment,
                        onNavigateToEvacuation = onNavigateToEvacuation,
                        onNavigateToMap = { selectedTab = MainTab.MAP },
                        onNavigateToResources = { selectedTab = MainTab.RESOURCES },
                        onNavigateToCalculator = onNavigateToCalculator,
                        onNavigateToGuides = onNavigateToGuides,
                        onNavigateToFirstAid = onNavigateToFirstAid,
                        onNavigateToSafeLocations = onNavigateToSafeLocations,
                        onNavigateToChecklist = onNavigateToChecklist,
                        onNavigateToFamily = onNavigateToFamily,
                        onNavigateToContacts = onNavigateToContacts,
                        onNavigateToSettings = onNavigateToSettings
                    )
                }

                MainTab.MAP -> {
                    OfflineMapScreen(
                        viewModel = viewModel,
                        onBack = { selectedTab = MainTab.HOME }
                    )
                }

                MainTab.RESOURCES -> {
                    ResourceManagerScreen(
                        viewModel = viewModel,
                        onBack = { selectedTab = MainTab.HOME },
                        onNavigateToCalculator = onNavigateToCalculator
                    )
                }

                MainTab.GUIDANCE -> {
                    ActionableGuidanceScreen(
                        onBack = { selectedTab = MainTab.HOME }
                    )
                }

                MainTab.MORE -> {
                    MoreMenuScreen(
                        currentLanguage = currentLanguage,
                        onNavigateToContacts = onNavigateToContacts,
                        onNavigateToGuides = onNavigateToGuides,
                        onNavigateToFirstAid = onNavigateToFirstAid,
                        onNavigateToFamily = onNavigateToFamily,
                        onNavigateToCalculator = onNavigateToCalculator,
                        onNavigateToAssessment = onNavigateToAssessment,
                        onNavigateToChecklist = onNavigateToChecklist,
                        onNavigateToSettings = onNavigateToSettings,
                        onOpenLanguageSheet = { showLanguageSheet = true }
                    )
                }
            }

            if (showLanguageSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showLanguageSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = SurfaceWhite,
                    dragHandle = { BottomSheetDefaults.DragHandle(color = BorderSubtle) }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Select Language / भाषा चुनें",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        AppLanguage.entries.forEach { language ->
                            val isSelected = language == currentLanguage
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MintLight else Color.Transparent)
                                    .clickable {
                                        viewModel.setLanguage(language)
                                        showLanguageSheet = false
                                    }
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = language.nativeName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = language.englishName,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MintDeep,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
            }
        }
    }
}
