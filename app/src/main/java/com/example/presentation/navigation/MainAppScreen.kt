package com.example.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.AppLanguage
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.guidance.ActionableGuidanceScreen
import com.example.presentation.map.OfflineMapScreen
import com.example.presentation.more.MoreMenuScreen
import com.example.presentation.resources.ResourceManagerScreen
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.JeevanBg

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
        }
    }
}
