package com.example.presentation.resources

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.db.ChecklistItemEntity
import com.example.domain.model.ResourceStatus
import com.example.presentation.predictions.SmartPredictionsView
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.JeevanBatteryAmber
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanEquipmentCyan
import com.example.ui.theme.JeevanFoodYellow
import com.example.ui.theme.JeevanFuelOrange
import com.example.ui.theme.JeevanMedicalRed
import com.example.ui.theme.JeevanRedBorder
import com.example.ui.theme.JeevanTextMuted
import com.example.ui.theme.JeevanWaterBlue
import java.util.Locale

private enum class ActiveDialog {
    NONE, ALL_SUPPLIES, WATER, FOOD, POWER, FUEL, MEDICAL, EQUIPMENT
}

@Composable
fun ResourceManagerScreen(
    viewModel: JeevanSetuViewModel,
    onBack: (() -> Unit)? = null,
    onNavigateToCalculator: (() -> Unit)? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Inventory, 1: Usage, 2: Projections
    var activeDialog by remember { mutableStateOf(ActiveDialog.NONE) }

    // Live Resource States from Room & ViewModel
    val water by viewModel.waterResource.collectAsStateWithLifecycle()
    val foodList by viewModel.foodItems.collectAsStateWithLifecycle()
    val power by viewModel.powerResource.collectAsStateWithLifecycle()
    val fuel by viewModel.fuelResource.collectAsStateWithLifecycle()
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()
    val checklist by viewModel.checklistItems.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Domain Engine Estimates
    val waterEstimate by viewModel.waterEstimate.collectAsStateWithLifecycle()
    val foodEstimate by viewModel.foodEstimate.collectAsStateWithLifecycle()
    val powerEstimate by viewModel.powerEstimate.collectAsStateWithLifecycle()
    val fuelEstimate by viewModel.fuelEstimate.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = JeevanBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("resources_screen")
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    text = "Resources",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Segmented Pill Tabs: [ Inventory | Usage | Projections ]
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Inventory", "Usage", "Projections").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) JeevanBrandGreen else Color(0xFF131F2B))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) JeevanBrandGreen else Color(0xFF1F2F40),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedTab = index }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.Black else Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // INVENTORY VIEW
                    val drinkingLiters = water?.drinkingWaterLiters ?: 0.0
                    val utilityLiters = water?.utilityWaterLiters ?: 0.0
                    val nonPerishMeals = foodList.filter { !it.isPerishable }.sumOf { it.estimatedMeals }
                    val perishMeals = foodList.filter { it.isPerishable }.sumOf { it.estimatedMeals }
                    val totalMeals = nonPerishMeals + perishMeals

                    val hasFirstAid = checklist.any { it.title.contains("First Aid", ignoreCase = true) && it.isCompleted }
                    val criticalMeds = medicines.filter { it.isCritical }

                    val essentialEquip = checklist.filter { it.category == "EVACUATION" || it.category == "HOME_SHELTER" }
                    val totalEquip = essentialEquip.size
                    val readyEquip = essentialEquip.count { it.isCompleted }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Water Card
                        val waterDaysStr = if (drinkingLiters <= 0.0) "0.0 days left" else String.format(Locale.US, "%.1f days left", waterEstimate.estimatedDaysDrinking)
                        val waterProgress = (waterEstimate.estimatedDaysDrinking / 7.0).toFloat().coerceIn(0f, 1f)
                        val waterPercentStr = "${(waterProgress * 100).toInt()}%"

                        ResourceItemCard(
                            icon = Icons.Default.WaterDrop,
                            iconBg = Color(0xFF0F2D3D),
                            iconColor = JeevanWaterBlue,
                            title = "Water",
                            subtitle = "${String.format(Locale.US, "%.1f", drinkingLiters)} L drinking • ${String.format(Locale.US, "%.1f", utilityLiters)} L utility",
                            daysLeft = waterDaysStr,
                            isWarning = waterEstimate.status == ResourceStatus.CRITICAL || drinkingLiters <= 0.0,
                            progress = waterProgress,
                            progressColor = JeevanWaterBlue,
                            percentLabel = waterPercentStr,
                            onClick = { activeDialog = ActiveDialog.WATER }
                        )

                        // 2. Food Card
                        val foodDaysStr = if (totalMeals <= 0) "0.0 days left" else String.format(Locale.US, "%.1f days left", foodEstimate.estimatedDaysRemaining)
                        val foodProgress = (foodEstimate.estimatedDaysRemaining / 7.0).toFloat().coerceIn(0f, 1f)
                        val foodPercentStr = "${(foodProgress * 100).toInt()}%"

                        ResourceItemCard(
                            icon = Icons.Default.Fastfood,
                            iconBg = Color(0xFF332612),
                            iconColor = JeevanFoodYellow,
                            title = "Food",
                            subtitle = if (totalMeals == 0) "0 meals available" else "$totalMeals meals ($nonPerishMeals rations, $perishMeals fresh)",
                            daysLeft = foodDaysStr,
                            isWarning = foodEstimate.status == ResourceStatus.CRITICAL || totalMeals <= 0,
                            progress = foodProgress,
                            progressColor = JeevanFoodYellow,
                            percentLabel = foodPercentStr,
                            onClick = { activeDialog = ActiveDialog.FOOD }
                        )

                        // 3. Battery / Power Card
                        val phoneBattery = power?.phoneBatteryPercent ?: 0
                        val hasPowerBank = power?.hasPowerBank == true
                        val powerBankPct = power?.powerBankPercent ?: 0
                        val powerBankMah = power?.powerBankCapacityMah ?: 0

                        val powerSubtitle = if (hasPowerBank) {
                            "Bank $powerBankPct% (${powerBankMah} mAh) • Phone $phoneBattery%"
                        } else {
                            "Phone $phoneBattery% • No backup bank"
                        }

                        val powerDaysStr = if (powerEstimate.estimatedHoursEco <= 0) "0 hrs left"
                        else if (powerEstimate.estimatedHoursEco >= 24) String.format(Locale.US, "%.1f days left", powerEstimate.estimatedHoursEco / 24.0)
                        else "${powerEstimate.estimatedHoursEco} hrs left"

                        val powerProgress = (phoneBattery / 100f).coerceIn(0f, 1f)

                        ResourceItemCard(
                            icon = Icons.Default.BatteryChargingFull,
                            iconBg = Color(0xFF362111),
                            iconColor = JeevanBatteryAmber,
                            title = "Battery / Power",
                            subtitle = powerSubtitle,
                            daysLeft = powerDaysStr,
                            isWarning = powerEstimate.status == ResourceStatus.CRITICAL || phoneBattery <= 15,
                            progress = powerProgress,
                            progressColor = JeevanBatteryAmber,
                            percentLabel = "$phoneBattery%",
                            onClick = { activeDialog = ActiveDialog.POWER }
                        )

                        // 4. Fuel Card
                        val hasVehicle = fuel?.hasVehicle == true
                        val fuelPercent = if (hasVehicle) (fuel?.fuelPercentage ?: 0) else 0
                        val fuelRange = if (hasVehicle) fuelEstimate.estimatedRangeKm.toInt() else 0
                        val fuelSubtitle = if (hasVehicle) {
                            "${fuel?.vehicleType ?: "Vehicle"} • $fuelPercent% tank"
                        } else {
                            "No vehicle registered"
                        }
                        val fuelDaysStr = if (hasVehicle) "$fuelRange km range" else "No vehicle"
                        val fuelProgress = if (hasVehicle) (fuelPercent / 100f).coerceIn(0f, 1f) else 0f

                        ResourceItemCard(
                            icon = Icons.Default.LocalGasStation,
                            iconBg = Color(0xFF331E12),
                            iconColor = JeevanFuelOrange,
                            title = "Fuel",
                            subtitle = fuelSubtitle,
                            daysLeft = fuelDaysStr,
                            isWarning = hasVehicle && (fuelEstimate.status == ResourceStatus.CRITICAL || fuelPercent <= 15),
                            progress = fuelProgress,
                            progressColor = JeevanFuelOrange,
                            percentLabel = if (hasVehicle) "$fuelPercent%" else "N/A",
                            onClick = { activeDialog = ActiveDialog.FUEL }
                        )

                        // 5. Medical Supplies Card
                        val medCount = medicines.size
                        val minMedDays = criticalMeds.minOfOrNull { it.daysRemaining }
                        val medSubtitle = "First Aid: ${if (hasFirstAid) "Equipped" else "Missing"} • ${if (medCount > 0) "$medCount prescription${if (medCount > 1) "s" else ""}" else "No prescriptions"}"
                        val medDaysStr = if (minMedDays != null) "$minMedDays days supply" else if (hasFirstAid) "Ready" else "Missing"
                        val medProgress = if (minMedDays != null) (minMedDays / 14f).coerceIn(0f, 1f) else if (hasFirstAid) 1f else 0f

                        ResourceItemCard(
                            icon = Icons.Default.MedicalServices,
                            iconBg = Color(0xFF38151A),
                            iconColor = JeevanMedicalRed,
                            title = "Medical Supplies",
                            subtitle = medSubtitle,
                            daysLeft = medDaysStr,
                            isWarning = !hasFirstAid || (minMedDays != null && minMedDays < 3),
                            progress = medProgress,
                            progressColor = Color(0xFF22C55E),
                            percentLabel = "${(medProgress * 100).toInt()}%",
                            onClick = { activeDialog = ActiveDialog.MEDICAL }
                        )

                        // 6. Equipment Card
                        val equipSubtitle = if (totalEquip == 0) "Checklist unconfigured" else "$readyEquip of $totalEquip items ready"
                        val equipDaysStr = if (totalEquip > 0 && readyEquip == totalEquip) "Ready" else "$readyEquip/$totalEquip"
                        val equipProgress = if (totalEquip > 0) (readyEquip.toFloat() / totalEquip).coerceIn(0f, 1f) else 0f

                        ResourceItemCard(
                            icon = Icons.Default.Build,
                            iconBg = Color(0xFF132B2B),
                            iconColor = JeevanEquipmentCyan,
                            title = "Equipment",
                            subtitle = equipSubtitle,
                            daysLeft = equipDaysStr,
                            isWarning = totalEquip > 0 && readyEquip < (totalEquip / 2),
                            progress = equipProgress,
                            progressColor = Color(0xFF22C55E),
                            percentLabel = "${(equipProgress * 100).toInt()}%",
                            onClick = { activeDialog = ActiveDialog.EQUIPMENT }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // + Add / Update Resources Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF121D28))
                                .border(1.2.dp, Color(0xFF25374C), RoundedCornerShape(14.dp))
                                .clickable { activeDialog = ActiveDialog.ALL_SUPPLIES }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Add / Update Resources",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }

                1 -> {
                    // USAGE TAB (Deterministic Household Burn Rate View)
                    UsageTabView(
                        profile = profile,
                        water = water,
                        foodList = foodList,
                        power = power,
                        fuel = fuel,
                        medicines = medicines,
                        waterEstimate = waterEstimate,
                        foodEstimate = foodEstimate,
                        powerEstimate = powerEstimate,
                        fuelEstimate = fuelEstimate
                    )
                }

                2 -> {
                    // PROJECTIONS TAB (Smart Predictions with Real Estimates)
                    SmartPredictionsView(
                        viewModel = viewModel,
                        onBack = null
                    )
                }
            }
        }
    }

    // Dialog Handlers
    when (activeDialog) {
        ActiveDialog.ALL_SUPPLIES -> {
            AllSuppliesUpdateDialog(
                currentWater = water,
                currentFoodList = foodList,
                currentPower = power,
                currentFuel = fuel,
                currentChecklist = checklist,
                onDismiss = { activeDialog = ActiveDialog.NONE },
                onSave = { drinking, utility, nonPerish, perish, hasBank, bankPct, bankMah, hasVeh, fuelPct, vehType, firstAid ->
                    viewModel.updateSupplies(
                        drinkingWater = drinking,
                        utilityWater = utility,
                        nonPerishableMeals = nonPerish,
                        perishableMeals = perish,
                        hasPowerBank = hasBank,
                        powerBankPercent = bankPct,
                        powerBankMah = bankMah,
                        hasVehicle = hasVeh,
                        fuelPercent = fuelPct,
                        vehicleType = vehType,
                        hasFirstAidKit = firstAid
                    )
                    activeDialog = ActiveDialog.NONE
                }
            )
        }

        ActiveDialog.WATER -> {
            WaterUpdateDialog(
                currentDrinking = water?.drinkingWaterLiters ?: 0.0,
                currentUtility = water?.utilityWaterLiters ?: 0.0,
                onDismiss = { activeDialog = ActiveDialog.NONE },
                onSave = { drinking, utility ->
                    viewModel.updateWaterLevels(drinking, utility)
                    activeDialog = ActiveDialog.NONE
                }
            )
        }

        ActiveDialog.FOOD -> {
            val curNonPerish = foodList.filter { !it.isPerishable }.sumOf { it.estimatedMeals }
            val curPerish = foodList.filter { it.isPerishable }.sumOf { it.estimatedMeals }
            FoodUpdateDialog(
                currentNonPerishable = curNonPerish,
                currentPerishable = curPerish,
                onDismiss = { activeDialog = ActiveDialog.NONE },
                onSave = { nonPerish, perish ->
                    viewModel.updateFoodRations(nonPerish, perish)
                    activeDialog = ActiveDialog.NONE
                }
            )
        }

        ActiveDialog.POWER -> {
            PowerUpdateDialog(
                currentPower = power,
                onDismiss = { activeDialog = ActiveDialog.NONE },
                onSave = { hasBank, bankMah, bankPct, flashlights, batteries ->
                    viewModel.updatePowerResources(
                        hasPowerBank = hasBank,
                        powerBankCapacityMah = bankMah,
                        powerBankPercent = bankPct,
                        flashlightCount = flashlights,
                        spareBatteriesCount = batteries
                    )
                    activeDialog = ActiveDialog.NONE
                }
            )
        }

        ActiveDialog.FUEL -> {
            FuelUpdateDialog(
                currentFuel = fuel,
                onDismiss = { activeDialog = ActiveDialog.NONE },
                onSave = { hasVeh, vehType, fuelPct ->
                    viewModel.updateFuelResources(
                        hasVehicle = hasVeh,
                        vehicleType = vehType,
                        fuelPercentage = fuelPct
                    )
                    activeDialog = ActiveDialog.NONE
                }
            )
        }

        ActiveDialog.MEDICAL -> {
            val hasFirstAid = checklist.any { it.title.contains("First Aid", ignoreCase = true) && it.isCompleted }
            val critMed = medicines.find { it.isCritical } ?: medicines.firstOrNull()
            MedicalUpdateDialog(
                currentFirstAid = hasFirstAid,
                currentMedName = critMed?.name ?: "",
                currentQuantity = critMed?.quantity ?: 0,
                currentDailyUsage = critMed?.dailyUsage ?: 1,
                onDismiss = { activeDialog = ActiveDialog.NONE },
                onSave = { firstAid, name, qty, usage ->
                    viewModel.updateMedicalSupplies(
                        hasFirstAidKit = firstAid,
                        criticalMedName = name,
                        criticalMedQuantity = qty,
                        criticalMedDailyUsage = usage
                    )
                    activeDialog = ActiveDialog.NONE
                }
            )
        }

        ActiveDialog.EQUIPMENT -> {
            val equipItems = checklist.filter { it.category == "EVACUATION" || it.category == "HOME_SHELTER" }
            EquipmentChecklistDialog(
                items = equipItems,
                onToggle = { item -> viewModel.toggleChecklistItem(item) },
                onDismiss = { activeDialog = ActiveDialog.NONE }
            )
        }

        ActiveDialog.NONE -> Unit
    }
}

// -------------------------------------------------------------
// Usage Tab View (Truthful deterministic burn rates)
// -------------------------------------------------------------
@Composable
private fun UsageTabView(
    profile: com.example.data.local.db.UserProfileEntity?,
    water: com.example.data.local.db.WaterResourceEntity?,
    foodList: List<com.example.data.local.db.FoodItemEntity>,
    power: com.example.data.local.db.PowerResourceEntity?,
    fuel: com.example.data.local.db.FuelResourceEntity?,
    medicines: List<com.example.data.local.db.MedicineEntity>,
    waterEstimate: com.example.domain.model.WaterEstimate,
    foodEstimate: com.example.domain.model.FoodEstimate,
    powerEstimate: com.example.domain.model.PowerEstimate,
    fuelEstimate: com.example.domain.model.FuelEstimate
) {
    val totalPeople = (profile?.numberOfAdults ?: 1) + (profile?.numberOfChildren ?: 0) + (profile?.numberOfElderly ?: 0)
    val dailyWaterDemand = totalPeople * 2.0 // 2.0 L/person/day
    val dailySanitationDemand = totalPeople * 3.0 // 3.0 L/person/day
    val dailyFoodDemand = totalPeople * 2 // 2 meals/person/day

    val currentDrinking = water?.drinkingWaterLiters ?: 0.0
    val currentUtility = water?.utilityWaterLiters ?: 0.0
    val nonPerishMeals = foodList.filter { !it.isPerishable }.sumOf { it.estimatedMeals }
    val perishMeals = foodList.filter { it.isPerishable }.sumOf { it.estimatedMeals }
    val totalMeals = nonPerishMeals + perishMeals

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Household Overview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(JeevanCard)
                .border(1.dp, JeevanCardBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = JeevanBrandGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Household Consumption Demographics",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Household Head Count: $totalPeople member${if (totalPeople > 1) "s" else ""} (${profile?.numberOfAdults ?: 1} Adults, ${profile?.numberOfChildren ?: 0} Children, ${profile?.numberOfElderly ?: 0} Seniors)",
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.sp
                )
                Text(
                    text = "Region: ${profile?.region ?: "General Region"} • Target Reserve: 7 Days Minimum",
                    color = JeevanTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Daily Burn Rates Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(JeevanCard)
                .border(1.dp, JeevanCardBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Daily Consumption & Burn Rates",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                // Water Burn Rate
                ConsumptionRateRow(
                    icon = Icons.Default.WaterDrop,
                    iconColor = JeevanWaterBlue,
                    title = "Water Demand Rate",
                    rate = "${String.format(Locale.US, "%.1f", dailyWaterDemand)} L / day drinking",
                    detail = "Current reserve: ${String.format(Locale.US, "%.1f", currentDrinking)} L drinking (${String.format(Locale.US, "%.1f", waterEstimate.estimatedDaysDrinking)} days) • ${String.format(Locale.US, "%.1f", currentUtility)} L sanitation"
                )

                // Food Burn Rate
                ConsumptionRateRow(
                    icon = Icons.Default.Fastfood,
                    iconColor = JeevanFoodYellow,
                    title = "Food Demand Rate",
                    rate = "$dailyFoodDemand meals / day (2 meals/person)",
                    detail = "Current provisions: $totalMeals meals ($nonPerishMeals sealed, $perishMeals fresh) • ${String.format(Locale.US, "%.1f", foodEstimate.estimatedDaysRemaining)} days reserve"
                )

                // Power Discharge Profile
                val powerBankMah = power?.powerBankCapacityMah ?: 0
                ConsumptionRateRow(
                    icon = Icons.Default.BatteryChargingFull,
                    iconColor = JeevanBatteryAmber,
                    title = "Power Discharge Profile",
                    rate = "Normal: ~14h runtime • Eco: ~36h + backup bank",
                    detail = "Phone: ${power?.phoneBatteryPercent ?: 0}% • Backup bank: ${if (power?.hasPowerBank == true) "$powerBankMah mAh (${power.powerBankPercent}%)" else "None"} • Est. runway: ${powerEstimate.estimatedHoursEco} hrs"
                )

                // Fuel Burn Rate
                if (fuel?.hasVehicle == true) {
                    ConsumptionRateRow(
                        icon = Icons.Default.LocalGasStation,
                        iconColor = JeevanFuelOrange,
                        title = "Fuel & Driving Range",
                        rate = "${fuel.vehicleType} • ${fuel.fuelPercentage}% tank",
                        detail = "Approx. evacuation driving range: ${fuelEstimate.estimatedRangeKm.toInt()} km. Reserved strictly for emergency movement."
                    )
                }

                // Medical Daily Prescriptions
                if (medicines.isNotEmpty()) {
                    val medLines = medicines.joinToString(", ") { "${it.name}: ${it.dailyUsage}/day (${it.daysRemaining}d)" }
                    ConsumptionRateRow(
                        icon = Icons.Default.MedicalServices,
                        iconColor = JeevanMedicalRed,
                        title = "Prescribed Daily Medicine",
                        rate = "${medicines.size} active prescription(s)",
                        detail = medLines
                    )
                }
            }
        }

        // Truthful Engine Notice Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF101B26))
                .border(1.dp, Color(0xFF1D2F42), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = JeevanBrandGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Deterministic Survival Model Active",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Consumption rates are calculated deterministically by the survival engine from household size and standard WHO/NDMA survival quotas. Automatic historical hardware telemetry sensors will integrate in subsequent update phases.",
                    color = JeevanTextMuted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ConsumptionRateRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    rate: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = rate,
                color = iconColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detail,
                color = JeevanTextMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

// -------------------------------------------------------------
// Resource Item Card
// -------------------------------------------------------------
@Composable
private fun ResourceItemCard(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    subtitle: String,
    daysLeft: String,
    isWarning: Boolean,
    progress: Float,
    progressColor: Color,
    percentLabel: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(JeevanCard)
            .border(1.dp, if (isWarning) JeevanRedBorder else JeevanCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = daysLeft,
                        color = if (isWarning) Color(0xFFF97316) else JeevanTextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isWarning) FontWeight.SemiBold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    color = JeevanTextMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF1E2F40))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress.coerceIn(0f, 1f))
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(progressColor)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = percentLabel,
                        color = JeevanTextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF475E77),
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// Comprehensive "All Supplies" Update Dialog
// -------------------------------------------------------------
@Composable
private fun AllSuppliesUpdateDialog(
    currentWater: com.example.data.local.db.WaterResourceEntity?,
    currentFoodList: List<com.example.data.local.db.FoodItemEntity>,
    currentPower: com.example.data.local.db.PowerResourceEntity?,
    currentFuel: com.example.data.local.db.FuelResourceEntity?,
    currentChecklist: List<ChecklistItemEntity>,
    onDismiss: () -> Unit,
    onSave: (
        drinking: Double,
        utility: Double,
        nonPerish: Int,
        perish: Int,
        hasBank: Boolean,
        bankPct: Int,
        bankMah: Int,
        hasVeh: Boolean,
        fuelPct: Int,
        vehType: String,
        firstAid: Boolean
    ) -> Unit
) {
    var drinkingStr by remember { mutableStateOf((currentWater?.drinkingWaterLiters ?: 0.0).toString()) }
    var utilityStr by remember { mutableStateOf((currentWater?.utilityWaterLiters ?: 0.0).toString()) }

    val initialNonPerish = currentFoodList.filter { !it.isPerishable }.sumOf { it.estimatedMeals }
    val initialPerish = currentFoodList.filter { it.isPerishable }.sumOf { it.estimatedMeals }
    var nonPerishStr by remember { mutableStateOf(initialNonPerish.toString()) }
    var perishStr by remember { mutableStateOf(initialPerish.toString()) }

    var hasPowerBank by remember { mutableStateOf(currentPower?.hasPowerBank == true) }
    var powerBankMahStr by remember { mutableStateOf((currentPower?.powerBankCapacityMah ?: 10000).toString()) }
    var powerBankPctStr by remember { mutableStateOf((currentPower?.powerBankPercent ?: 100).toString()) }

    var hasVehicle by remember { mutableStateOf(currentFuel?.hasVehicle == true) }
    var vehicleType by remember { mutableStateOf(currentFuel?.vehicleType ?: "4-Wheeler (Car / SUV)") }
    var fuelPercentStr by remember { mutableStateOf((currentFuel?.fuelPercentage ?: 50).toString()) }

    val initialFirstAid = currentChecklist.any { it.title.contains("First Aid", ignoreCase = true) && it.isCompleted }
    var hasFirstAid by remember { mutableStateOf(initialFirstAid) }

    // Validation errors
    var drinkingErr by remember { mutableStateOf<String?>(null) }
    var utilityErr by remember { mutableStateOf<String?>(null) }
    var nonPerishErr by remember { mutableStateOf<String?>(null) }
    var perishErr by remember { mutableStateOf<String?>(null) }
    var bankPctErr by remember { mutableStateOf<String?>(null) }
    var bankMahErr by remember { mutableStateOf<String?>(null) }
    var fuelPctErr by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text(
                text = "Update Supplies Inventory",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section: Water
                Text("WATER RESERVES", color = JeevanWaterBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                ValidatingTextField(
                    value = drinkingStr,
                    onValueChange = {
                        drinkingStr = it
                        drinkingErr = validatePositiveDouble(it)
                    },
                    label = "Drinking Water (Litres)",
                    errorMessage = drinkingErr
                )

                ValidatingTextField(
                    value = utilityStr,
                    onValueChange = {
                        utilityStr = it
                        utilityErr = validatePositiveDouble(it)
                    },
                    label = "Sanitation / Utility Water (Litres)",
                    errorMessage = utilityErr
                )

                // Section: Food
                Spacer(modifier = Modifier.height(4.dp))
                Text("FOOD RATIONS", color = JeevanFoodYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                ValidatingTextField(
                    value = nonPerishStr,
                    onValueChange = {
                        nonPerishStr = it
                        nonPerishErr = validatePositiveInt(it)
                    },
                    label = "Non-Perishable Rations (meals)",
                    errorMessage = nonPerishErr
                )

                ValidatingTextField(
                    value = perishStr,
                    onValueChange = {
                        perishStr = it
                        perishErr = validatePositiveInt(it)
                    },
                    label = "Fresh / Perishable Food (meals)",
                    errorMessage = perishErr
                )

                // Section: Power
                Spacer(modifier = Modifier.height(4.dp))
                Text("BACKUP POWER", color = JeevanBatteryAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Portable Power Bank", color = Color.White, fontSize = 13.sp)
                    Switch(
                        checked = hasPowerBank,
                        onCheckedChange = { hasPowerBank = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = JeevanBrandGreen
                        )
                    )
                }

                if (hasPowerBank) {
                    ValidatingTextField(
                        value = powerBankPctStr,
                        onValueChange = {
                            powerBankPctStr = it
                            bankPctErr = validatePercent(it)
                        },
                        label = "Power Bank Charge (%)",
                        errorMessage = bankPctErr
                    )

                    ValidatingTextField(
                        value = powerBankMahStr,
                        onValueChange = {
                            powerBankMahStr = it
                            bankMahErr = validatePositiveInt(it, max = 100000)
                        },
                        label = "Power Bank Capacity (mAh)",
                        errorMessage = bankMahErr
                    )
                }

                // Section: Fuel
                Spacer(modifier = Modifier.height(4.dp))
                Text("EVACUATION VEHICLE & FUEL", color = JeevanFuelOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vehicle Available", color = Color.White, fontSize = 13.sp)
                    Switch(
                        checked = hasVehicle,
                        onCheckedChange = { hasVehicle = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = JeevanBrandGreen
                        )
                    )
                }

                if (hasVehicle) {
                    ValidatingTextField(
                        value = fuelPercentStr,
                        onValueChange = {
                            fuelPercentStr = it
                            fuelPctErr = validatePercent(it)
                        },
                        label = "Fuel Tank Level (%)",
                        errorMessage = fuelPctErr
                    )
                }

                // Section: Medical
                Spacer(modifier = Modifier.height(4.dp))
                Text("MEDICAL READINESS", color = JeevanMedicalRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("First Aid Kit Equipped", color = Color.White, fontSize = 13.sp)
                    Switch(
                        checked = hasFirstAid,
                        onCheckedChange = { hasFirstAid = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = JeevanBrandGreen
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Run complete validation
                    val dErr = validatePositiveDouble(drinkingStr)
                    val uErr = validatePositiveDouble(utilityStr)
                    val npErr = validatePositiveInt(nonPerishStr)
                    val pErr = validatePositiveInt(perishStr)
                    val bpErr = if (hasPowerBank) validatePercent(powerBankPctStr) else null
                    val bmErr = if (hasPowerBank) validatePositiveInt(powerBankMahStr, max = 100000) else null
                    val fpErr = if (hasVehicle) validatePercent(fuelPercentStr) else null

                    drinkingErr = dErr
                    utilityErr = uErr
                    nonPerishErr = npErr
                    perishErr = pErr
                    bankPctErr = bpErr
                    bankMahErr = bmErr
                    fuelPctErr = fpErr

                    val hasAnyError = dErr != null || uErr != null || npErr != null || pErr != null || bpErr != null || bmErr != null || fpErr != null

                    if (!hasAnyError) {
                        onSave(
                            drinkingStr.toDouble(),
                            utilityStr.toDouble(),
                            nonPerishStr.toInt(),
                            perishStr.toInt(),
                            hasPowerBank,
                            if (hasPowerBank) powerBankPctStr.toInt() else 0,
                            if (hasPowerBank) powerBankMahStr.toInt() else 0,
                            hasVehicle,
                            if (hasVehicle) fuelPercentStr.toInt() else 0,
                            vehicleType,
                            hasFirstAid
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF223447))
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

// -------------------------------------------------------------
// Individual Resource Dialogs (Water, Food, Power, Fuel, Med, Equip)
// -------------------------------------------------------------
@Composable
private fun WaterUpdateDialog(
    currentDrinking: Double,
    currentUtility: Double,
    onDismiss: () -> Unit,
    onSave: (Double, Double) -> Unit
) {
    var drinkingStr by remember { mutableStateOf(currentDrinking.toString()) }
    var utilityStr by remember { mutableStateOf(currentUtility.toString()) }
    var drinkingErr by remember { mutableStateOf<String?>(null) }
    var utilityErr by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text("Update Water Supplies", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ValidatingTextField(
                    value = drinkingStr,
                    onValueChange = {
                        drinkingStr = it
                        drinkingErr = validatePositiveDouble(it)
                    },
                    label = "Drinking Water (Litres)",
                    errorMessage = drinkingErr
                )

                // Quick buttons for drinking water
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("+1L" to 1.0, "+5L" to 5.0, "-1L" to -1.0, "Empty" to null).forEach { (label, diff) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1F2F40))
                                .clickable {
                                    if (diff == null) {
                                        drinkingStr = "0.0"
                                    } else {
                                        val cur = drinkingStr.toDoubleOrNull() ?: 0.0
                                        drinkingStr = (cur + diff).coerceAtLeast(0.0).toString()
                                    }
                                    drinkingErr = null
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(label, color = Color(0xFF93C5FD), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                ValidatingTextField(
                    value = utilityStr,
                    onValueChange = {
                        utilityStr = it
                        utilityErr = validatePositiveDouble(it)
                    },
                    label = "Utility / Sanitation Water (Litres)",
                    errorMessage = utilityErr
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dErr = validatePositiveDouble(drinkingStr)
                    val uErr = validatePositiveDouble(utilityStr)
                    drinkingErr = dErr
                    utilityErr = uErr
                    if (dErr == null && uErr == null) {
                        onSave(drinkingStr.toDouble(), utilityStr.toDouble())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF223447))) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

@Composable
private fun FoodUpdateDialog(
    currentNonPerishable: Int,
    currentPerishable: Int,
    onDismiss: () -> Unit,
    onSave: (Int, Int) -> Unit
) {
    var nonPerishStr by remember { mutableStateOf(currentNonPerishable.toString()) }
    var perishStr by remember { mutableStateOf(currentPerishable.toString()) }
    var nonPerishErr by remember { mutableStateOf<String?>(null) }
    var perishErr by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text("Update Food Provisions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ValidatingTextField(
                    value = nonPerishStr,
                    onValueChange = {
                        nonPerishStr = it
                        nonPerishErr = validatePositiveInt(it)
                    },
                    label = "Non-Perishable Rations (meals)",
                    errorMessage = nonPerishErr
                )

                // Quick buttons for non-perishable
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("+2 meals" to 2, "+6 meals" to 6, "Empty" to null).forEach { (label, diff) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF1F2F40))
                                .clickable {
                                    if (diff == null) {
                                        nonPerishStr = "0"
                                    } else {
                                        val cur = nonPerishStr.toIntOrNull() ?: 0
                                        nonPerishStr = (cur + diff).coerceAtLeast(0).toString()
                                    }
                                    nonPerishErr = null
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(label, color = JeevanFoodYellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                ValidatingTextField(
                    value = perishStr,
                    onValueChange = {
                        perishStr = it
                        perishErr = validatePositiveInt(it)
                    },
                    label = "Fresh / Perishable Food (meals)",
                    errorMessage = perishErr
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val npErr = validatePositiveInt(nonPerishStr)
                    val pErr = validatePositiveInt(perishStr)
                    nonPerishErr = npErr
                    perishErr = pErr
                    if (npErr == null && pErr == null) {
                        onSave(nonPerishStr.toInt(), perishStr.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF223447))) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

@Composable
private fun PowerUpdateDialog(
    currentPower: com.example.data.local.db.PowerResourceEntity?,
    onDismiss: () -> Unit,
    onSave: (hasBank: Boolean, bankMah: Int, bankPct: Int, flashlights: Int, batteries: Int) -> Unit
) {
    var hasBank by remember { mutableStateOf(currentPower?.hasPowerBank == true) }
    var bankMahStr by remember { mutableStateOf((currentPower?.powerBankCapacityMah ?: 10000).toString()) }
    var bankPctStr by remember { mutableStateOf((currentPower?.powerBankPercent ?: 100).toString()) }
    var flashlightsStr by remember { mutableStateOf((currentPower?.flashlightCount ?: 1).toString()) }
    var batteriesStr by remember { mutableStateOf((currentPower?.spareBatteriesCount ?: 4).toString()) }

    var bankMahErr by remember { mutableStateOf<String?>(null) }
    var bankPctErr by remember { mutableStateOf<String?>(null) }
    var flashlightsErr by remember { mutableStateOf<String?>(null) }
    var batteriesErr by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text("Update Emergency Power", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Device battery telemetry info (read-only telemetry indicator)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1C2B3A))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Phone Battery: ${currentPower?.phoneBatteryPercent ?: 0}% (Device Telemetry)",
                        color = Color(0xFF93C5FD),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Power Bank Available", color = Color.White, fontSize = 14.sp)
                    Switch(
                        checked = hasBank,
                        onCheckedChange = { hasBank = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = JeevanBrandGreen)
                    )
                }

                if (hasBank) {
                    ValidatingTextField(
                        value = bankPctStr,
                        onValueChange = {
                            bankPctStr = it
                            bankPctErr = validatePercent(it)
                        },
                        label = "Power Bank Charge (%)",
                        errorMessage = bankPctErr
                    )

                    ValidatingTextField(
                        value = bankMahStr,
                        onValueChange = {
                            bankMahStr = it
                            bankMahErr = validatePositiveInt(it, max = 100000)
                        },
                        label = "Power Bank Capacity (mAh)",
                        errorMessage = bankMahErr
                    )
                }

                ValidatingTextField(
                    value = flashlightsStr,
                    onValueChange = {
                        flashlightsStr = it
                        flashlightsErr = validatePositiveInt(it, max = 50)
                    },
                    label = "Emergency Flashlights Count",
                    errorMessage = flashlightsErr
                )

                ValidatingTextField(
                    value = batteriesStr,
                    onValueChange = {
                        batteriesStr = it
                        batteriesErr = validatePositiveInt(it, max = 100)
                    },
                    label = "Spare Batteries Count",
                    errorMessage = batteriesErr
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val bpErr = if (hasBank) validatePercent(bankPctStr) else null
                    val bmErr = if (hasBank) validatePositiveInt(bankMahStr, max = 100000) else null
                    val flErr = validatePositiveInt(flashlightsStr, max = 50)
                    val btErr = validatePositiveInt(batteriesStr, max = 100)

                    bankPctErr = bpErr
                    bankMahErr = bmErr
                    flashlightsErr = flErr
                    batteriesErr = btErr

                    if (bpErr == null && bmErr == null && flErr == null && btErr == null) {
                        onSave(
                            hasBank,
                            if (hasBank) bankMahStr.toInt() else 0,
                            if (hasBank) bankPctStr.toInt() else 0,
                            flashlightsStr.toInt(),
                            batteriesStr.toInt()
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF223447))) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

@Composable
private fun FuelUpdateDialog(
    currentFuel: com.example.data.local.db.FuelResourceEntity?,
    onDismiss: () -> Unit,
    onSave: (hasVeh: Boolean, vehType: String, fuelPct: Int) -> Unit
) {
    var hasVehicle by remember { mutableStateOf(currentFuel?.hasVehicle == true) }
    var vehicleType by remember { mutableStateOf(currentFuel?.vehicleType ?: "4-Wheeler (Car / SUV)") }
    var fuelPercentStr by remember { mutableStateOf((currentFuel?.fuelPercentage ?: 50).toString()) }
    var fuelErr by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text("Update Vehicle & Fuel", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Evacuation Vehicle Available", color = Color.White, fontSize = 14.sp)
                    Switch(
                        checked = hasVehicle,
                        onCheckedChange = { hasVehicle = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = JeevanBrandGreen)
                    )
                }

                if (hasVehicle) {
                    Text("Vehicle Type", color = JeevanTextMuted, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("4-Wheeler", "2-Wheeler").forEach { type ->
                            val isSel = vehicleType.contains(type, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) JeevanBrandGreen else Color(0xFF1E2F40))
                                    .clickable { vehicleType = if (type == "4-Wheeler") "4-Wheeler (Car / SUV)" else "2-Wheeler (Motorcycle / Scooter)" }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSel) Color.Black else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    ValidatingTextField(
                        value = fuelPercentStr,
                        onValueChange = {
                            fuelPercentStr = it
                            fuelErr = validatePercent(it)
                        },
                        label = "Fuel Tank Level (%)",
                        errorMessage = fuelErr
                    )

                    val pctVal = fuelPercentStr.toIntOrNull() ?: 0
                    val estRange = (pctVal / 100.0) * 450.0
                    Text(
                        text = "Estimated driving range: ${estRange.toInt()} km",
                        color = Color(0xFF93C5FD),
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fErr = if (hasVehicle) validatePercent(fuelPercentStr) else null
                    fuelErr = fErr
                    if (fErr == null) {
                        onSave(
                            hasVehicle,
                            vehicleType,
                            if (hasVehicle) fuelPercentStr.toInt() else 0
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF223447))) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

@Composable
private fun MedicalUpdateDialog(
    currentFirstAid: Boolean,
    currentMedName: String,
    currentQuantity: Int,
    currentDailyUsage: Int,
    onDismiss: () -> Unit,
    onSave: (firstAid: Boolean, name: String, qty: Int, usage: Int) -> Unit
) {
    var hasFirstAid by remember { mutableStateOf(currentFirstAid) }
    var medName by remember { mutableStateOf(currentMedName) }
    var quantityStr by remember { mutableStateOf(currentQuantity.toString()) }
    var usageStr by remember { mutableStateOf(currentDailyUsage.toString()) }

    var qtyErr by remember { mutableStateOf<String?>(null) }
    var usageErr by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text("Update Medical Supplies", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("First Aid Kit Equipped", color = Color.White, fontSize = 14.sp)
                    Switch(
                        checked = hasFirstAid,
                        onCheckedChange = { hasFirstAid = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = JeevanBrandGreen)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("Critical Prescription Medicine", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = medName,
                    onValueChange = { medName = it },
                    label = { Text("Medicine Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = JeevanBrandGreen,
                        unfocusedBorderColor = Color(0xFF2E4155),
                        focusedLabelColor = JeevanBrandGreen,
                        unfocusedLabelColor = JeevanTextMuted
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                ValidatingTextField(
                    value = quantityStr,
                    onValueChange = {
                        quantityStr = it
                        qtyErr = validatePositiveInt(it)
                    },
                    label = "Quantity (Pills / Units)",
                    errorMessage = qtyErr
                )

                ValidatingTextField(
                    value = usageStr,
                    onValueChange = {
                        usageStr = it
                        usageErr = validatePositiveInt(it, allowZero = false)
                    },
                    label = "Daily Dosage (Units/Day)",
                    errorMessage = usageErr
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qErr = validatePositiveInt(quantityStr)
                    val uErr = validatePositiveInt(usageStr, allowZero = false)
                    qtyErr = qErr
                    usageErr = uErr
                    if (qErr == null && uErr == null) {
                        onSave(
                            hasFirstAid,
                            medName,
                            quantityStr.toInt(),
                            usageStr.toInt()
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF223447))) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}

@Composable
private fun EquipmentChecklistDialog(
    items: List<ChecklistItemEntity>,
    onToggle: (ChecklistItemEntity) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF15222E),
        title = {
            Text("Emergency Equipment Readiness", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (items.isEmpty()) {
                    Text("No essential equipment configured.", color = JeevanTextMuted, fontSize = 13.sp)
                } else {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (item.isCompleted) Color(0xFF183329) else Color(0xFF1B2836))
                                .clickable { onToggle(item) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { onToggle(item) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = JeevanBrandGreen,
                                    checkmarkColor = Color.Black,
                                    uncheckedColor = Color(0xFF475E77)
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.title,
                                color = if (item.isCompleted) Color.White else Color(0xFFCBD5E1),
                                fontSize = 13.sp,
                                fontWeight = if (item.isCompleted) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = JeevanBrandGreen)
            ) {
                Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// -------------------------------------------------------------
// Validation Helpers & Component
// -------------------------------------------------------------
@Composable
private fun ValidatingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = errorMessage != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = JeevanBrandGreen,
                unfocusedBorderColor = Color(0xFF2E4155),
                focusedLabelColor = JeevanBrandGreen,
                unfocusedLabelColor = JeevanTextMuted,
                errorBorderColor = Color(0xFFEF4444),
                errorLabelColor = Color(0xFFEF4444),
                cursorColor = JeevanBrandGreen
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorMessage,
                color = Color(0xFFEF4444),
                fontSize = 11.sp
            )
        }
    }
}

private fun validatePositiveDouble(input: String, max: Double = 10000.0, allowZero: Boolean = true): String? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return "Value cannot be empty"
    val num = trimmed.toDoubleOrNull() ?: return "Please enter a valid number"
    if (num < 0.0) return "Value cannot be negative"
    if (!allowZero && num == 0.0) return "Value must be greater than zero"
    if (num > max) return "Value cannot exceed ${max.toInt()}"
    return null
}

private fun validatePositiveInt(input: String, max: Int = 10000, allowZero: Boolean = true): String? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return "Value cannot be empty"
    val num = trimmed.toIntOrNull() ?: return "Please enter a valid whole number"
    if (num < 0) return "Value cannot be negative"
    if (!allowZero && num == 0) return "Value must be greater than zero"
    if (num > max) return "Value cannot exceed $max"
    return null
}

private fun validatePercent(input: String): String? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return "Percentage cannot be empty"
    val num = trimmed.toIntOrNull() ?: return "Must be an integer between 0 and 100"
    if (num !in 0..100) return "Percentage must be between 0 and 100"
    return null
}
