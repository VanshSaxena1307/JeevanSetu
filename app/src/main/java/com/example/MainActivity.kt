package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.domain.model.DisasterGuide
import com.example.domain.model.FirstAidTopic
import com.example.presentation.assessment.AssessmentScreen
import com.example.presentation.checklist.ChecklistScreen
import com.example.presentation.contacts.EmergencyContactsScreen
import com.example.presentation.dashboard.DashboardScreen
import com.example.presentation.evacuation.EvacuationScreen
import com.example.presentation.family.FamilyProfileScreen
import com.example.presentation.firstaid.FirstAidDetailScreen
import com.example.presentation.firstaid.FirstAidScreen
import com.example.presentation.guides.DisasterGuidesScreen
import com.example.presentation.guides.GuideDetailScreen
import com.example.presentation.locations.SafeLocationsScreen
import com.example.presentation.map.OfflineMapScreen
import com.example.presentation.navigation.MainAppScreen
import com.example.presentation.resources.ResourceManagerScreen
import com.example.presentation.resources.SurvivalCalculatorScreen
import com.example.presentation.settings.SettingsScreen
import com.example.presentation.setup.FirstTimeSetupWizard
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanSetuTheme
import com.example.ui.theme.Slate950

class MainActivity : ComponentActivity() {

    private val viewModel: JeevanSetuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JeevanSetuTheme {
                JeevanSetuApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun JeevanSetuApp(viewModel: JeevanSetuViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    // Request Location Permission Gracefully
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.refreshLocation()
        }
    }

    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!fineGranted) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    var selectedGuide by remember { mutableStateOf<DisasterGuide?>(null) }
    var selectedFirstAidTopic by remember { mutableStateOf<FirstAidTopic?>(null) }

    if (profile == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Slate950),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = JeevanBrandGreen,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "JEEVAN SETU",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                )
                CircularProgressIndicator(
                    color = JeevanBrandGreen,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            }
        }
        return
    }

    val startDestination = if (!profile!!.isSetupCompleted) "setup" else "dashboard"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable("setup") {
            FirstTimeSetupWizard(
                viewModel = viewModel,
                onComplete = {
                    navController.navigate("dashboard") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            MainAppScreen(
                viewModel = viewModel,
                onNavigateToAssessment = { navController.navigate("assessment") },
                onNavigateToEvacuation = { navController.navigate("evacuation") },
                onNavigateToCalculator = { navController.navigate("calculator") },
                onNavigateToGuides = { navController.navigate("guides") },
                onNavigateToFirstAid = { navController.navigate("first_aid") },
                onNavigateToSafeLocations = { navController.navigate("safe_locations") },
                onNavigateToChecklist = { navController.navigate("checklist") },
                onNavigateToFamily = { navController.navigate("family") },
                onNavigateToContacts = { navController.navigate("contacts") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }

        composable("assessment") {
            AssessmentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate("map") },
                onNavigateToGuides = { navController.navigate("guides") }
            )
        }

        composable("evacuation") {
            EvacuationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate("map") },
                onNavigateToSafeLocations = { navController.navigate("safe_locations") }
            )
        }

        composable("resources") {
            ResourceManagerScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCalculator = { navController.navigate("calculator") }
            )
        }

        composable("calculator") {
            SurvivalCalculatorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToFamily = { navController.navigate("family") },
                onNavigateToResources = { navController.navigate("resources") }
            )
        }

        composable("map") {
            OfflineMapScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("safe_locations") {
            SafeLocationsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate("map") }
            )
        }

        composable("guides") {
            DisasterGuidesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSelectGuide = { guide ->
                    selectedGuide = guide
                    navController.navigate("guide_detail")
                }
            )
        }

        composable("guide_detail") {
            selectedGuide?.let { guide ->
                GuideDetailScreen(
                    guide = guide,
                    isOnline = isOnline,
                    onBack = { navController.popBackStack() }
                )
            } ?: run {
                navController.popBackStack()
            }
        }

        composable("first_aid") {
            FirstAidScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSelectTopic = { topic ->
                    selectedFirstAidTopic = topic
                    navController.navigate("first_aid_detail")
                }
            )
        }

        composable("first_aid_detail") {
            selectedFirstAidTopic?.let { topic ->
                FirstAidDetailScreen(
                    topic = topic,
                    isOnline = isOnline,
                    onBack = { navController.popBackStack() }
                )
            } ?: run {
                navController.popBackStack()
            }
        }

        composable("checklist") {
            ChecklistScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("family") {
            FamilyProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("contacts") {
            EmergencyContactsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToSetup = { navController.navigate("setup") }
            )
        }
    }
}
