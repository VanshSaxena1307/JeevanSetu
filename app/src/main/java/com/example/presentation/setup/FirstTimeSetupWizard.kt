package com.example.presentation.setup

import android.content.Context
import android.os.BatteryManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.components.SafetyDisclaimerCard
import com.example.presentation.components.TacticalInputField
import com.example.presentation.viewmodel.JeevanSetuViewModel
import com.example.ui.theme.CautionAmber
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.JeevanBatteryAmber
import com.example.ui.theme.JeevanBg
import com.example.ui.theme.JeevanBrandGreen
import com.example.ui.theme.JeevanCard
import com.example.ui.theme.JeevanCardBorder
import com.example.ui.theme.JeevanEquipmentCyan
import com.example.ui.theme.JeevanFoodYellow
import com.example.ui.theme.JeevanFuelOrange
import com.example.ui.theme.JeevanGreenBg
import com.example.ui.theme.JeevanGreenBorder
import com.example.ui.theme.JeevanMedicalRed
import com.example.ui.theme.JeevanTextMuted
import com.example.ui.theme.JeevanWaterBlue
import com.example.ui.theme.RescueCyan
import com.example.ui.theme.SafetyGreen
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.WarningOrange

@Composable
fun FirstTimeSetupWizard(
    viewModel: JeevanSetuViewModel,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 8
    var isSaving by remember { mutableStateOf(false) }

    // Step 1: Household
    var adults by remember { mutableIntStateOf(2) }
    var children by remember { mutableIntStateOf(0) }
    var elderly by remember { mutableIntStateOf(0) }
    var injured by remember { mutableIntStateOf(0) }
    var specialNeeds by remember { mutableStateOf("") }
    val totalPeople by remember { derivedStateOf { (adults + children + elderly).coerceAtLeast(1) } }

    // Step 2: Location / Region
    val regions = listOf(
        "Northern Plains / Delhi NCR",
        "Western Coastal / Mumbai & Konkan",
        "Himalayan Foothills / Uttarakhand",
        "Eastern Delta / Bengal & Odisha",
        "Southern Peninsula / Chennai & Bengaluru",
        "Custom Location"
    )
    var selectedRegion by remember { mutableStateOf(regions[0]) }
    var customRegionName by remember { mutableStateOf("") }

    // Step 3: Water
    var drinkingWaterInput by remember { mutableStateOf("12") }
    var utilityWaterInput by remember { mutableStateOf("20") }
    val drinkingWaterLiters by remember { derivedStateOf { drinkingWaterInput.toDoubleOrNull() ?: 0.0 } }
    val utilityWaterLiters by remember { derivedStateOf { utilityWaterInput.toDoubleOrNull() ?: 0.0 } }
    val waterDaysRemaining by remember {
        derivedStateOf {
            val dailyNeed = totalPeople * 2.0
            if (dailyNeed <= 0 || drinkingWaterLiters <= 0.0) 0.0 else drinkingWaterLiters / dailyNeed
        }
    }

    // Step 4: Food
    var nonPerishableMealsInput by remember { mutableStateOf("10") }
    var perishableMealsInput by remember { mutableStateOf("4") }
    val nonPerishableMeals by remember { derivedStateOf { nonPerishableMealsInput.toIntOrNull() ?: 0 } }
    val perishableMeals by remember { derivedStateOf { perishableMealsInput.toIntOrNull() ?: 0 } }
    val totalMeals by remember { derivedStateOf { nonPerishableMeals + perishableMeals } }
    val foodDaysRemaining by remember {
        derivedStateOf {
            val dailyNeed = totalPeople * 2.0
            if (dailyNeed <= 0 || totalMeals <= 0) 0.0 else totalMeals.toDouble() / dailyNeed
        }
    }

    // Step 5: Power
    val systemBattery = remember {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)?.takeIf { it in 1..100 } ?: 85
    }
    var phoneBatteryPercent by remember { mutableIntStateOf(systemBattery) }
    var hasPowerBank by remember { mutableStateOf(true) }
    var powerBankMah by remember { mutableIntStateOf(10000) }
    var powerBankPercent by remember { mutableIntStateOf(100) }
    var flashlightCount by remember { mutableIntStateOf(1) }
    var spareBatteriesCount by remember { mutableIntStateOf(4) }

    // Step 6: Fuel
    var hasVehicle by remember { mutableStateOf(true) }
    val vehicleTypes = listOf("Car / Sedan", "SUV / 4x4", "Two-Wheeler / Scooter", "Van / Pickup")
    var selectedVehicleType by remember { mutableStateOf(vehicleTypes[0]) }
    var fuelPercent by remember { mutableIntStateOf(50) }
    val estimatedRangeKm by remember { derivedStateOf { if (hasVehicle) (fuelPercent / 100.0) * 450.0 else 0.0 } }

    // Step 7: Medical Supplies
    var hasFirstAidKit by remember { mutableStateOf(true) }
    var hasCriticalPrescription by remember { mutableStateOf(false) }
    var medName by remember { mutableStateOf("") }
    var medQtyInput by remember { mutableStateOf("30") }
    var medDailyInput by remember { mutableStateOf("1") }
    val medQty by remember { derivedStateOf { medQtyInput.toIntOrNull() ?: 0 } }
    val medDaily by remember { derivedStateOf { medDailyInput.toIntOrNull() ?: 1 } }
    val medDaysRemaining by remember {
        derivedStateOf {
            if (medDaily > 0 && medQty > 0) medQty / medDaily else 0
        }
    }

    // Step 8: Equipment Checklist
    val defaultGear = remember {
        listOf(
            "Emergency LED Flashlight / Torch" to true,
            "AM/FM Radio" to false,
            "Emergency Whistle" to true,
            "Multi-tool / Knife" to true,
            "Heavy-Duty Work Gloves" to false,
            "Waterproof Matchbox / Lighter" to true,
            "Thermal Space Blanket" to false
        )
    }
    val gearState = remember { mutableStateOf(defaultGear.toMap().toMutableMap()) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("first_time_setup_wizard"),
        color = Slate950
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Step Progress Bar & Step Label
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(JeevanBrandGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = JeevanBrandGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "JEEVAN SETU",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.5.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Slate800)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "STEP $currentStep OF $totalSteps",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = JeevanBrandGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Step Progress Indicator Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 1..totalSteps) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    when {
                                        i < currentStep -> JeevanBrandGreen
                                        i == currentStep -> JeevanBrandGreen
                                        else -> Slate700
                                    }
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step Content (Scrollable Container)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (currentStep) {
                        // -------------------------------------------------------------
                        // STEP 1: HOUSEHOLD & DEPENDENTS
                        // -------------------------------------------------------------
                        1 -> {
                            StepHeader(
                                stepNumber = 1,
                                category = "HOUSEHOLD & DEPENDENTS",
                                title = "How many people are in your care?",
                                subtitle = "Accurate headcount ensures drinking water and food survival quotas are properly calculated for everyone.",
                                icon = Icons.Default.Group,
                                accentColor = JeevanBrandGreen
                            )

                            // Steppers
                            SetupStepperRow(
                                label = "Adults (18–64)",
                                count = adults,
                                min = 1,
                                onIncrement = { adults++ },
                                onDecrement = { if (adults > 1) adults-- },
                                icon = Icons.Default.Group,
                                accentColor = JeevanBrandGreen
                            )

                            SetupStepperRow(
                                label = "Children (< 18)",
                                count = children,
                                min = 0,
                                onIncrement = { children++ },
                                onDecrement = { if (children > 0) children-- },
                                icon = Icons.Default.Group,
                                accentColor = RescueCyan
                            )

                            SetupStepperRow(
                                label = "Elderly (65+)",
                                count = elderly,
                                min = 0,
                                onIncrement = { elderly++ },
                                onDecrement = { if (elderly > 0) elderly-- },
                                icon = Icons.Default.Group,
                                accentColor = CautionAmber
                            )

                            SetupStepperRow(
                                label = "Injured / Non-Ambulatory",
                                count = injured,
                                min = 0,
                                onIncrement = { injured++ },
                                onDecrement = { if (injured > 0) injured-- },
                                icon = Icons.Default.Warning,
                                accentColor = EmergencyRed
                            )

                            // Special Needs text input
                            TacticalInputField(
                                value = specialNeeds,
                                onValueChange = { specialNeeds = it },
                                label = "Special Needs (Optional)",
                                placeholder = "e.g. Wheelchair, dialysis, infant formula",
                                singleLine = false,
                                maxLines = 3,
                                accentColor = JeevanBrandGreen
                            )

                            // Headcount summary chip
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total Family Headcount:",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFCBD5E1))
                                    )
                                    Text(
                                        text = "$totalPeople Persons",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = JeevanBrandGreen,
                                            fontWeight = FontWeight.Black
                                        )
                                    )
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // STEP 2: LOCATION & REGION
                        // -------------------------------------------------------------
                        2 -> {
                            StepHeader(
                                stepNumber = 2,
                                category = "LOCATION & TERRAIN",
                                title = "Select your primary location zone",
                                subtitle = "Configures offline disaster risk maps, high-ground shelters, and local emergency helplines.",
                                icon = Icons.Default.LocationOn,
                                accentColor = WarningOrange
                            )

                            regions.forEach { r ->
                                val isSelected = selectedRegion == r
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedRegion = r },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) WarningOrange.copy(alpha = 0.15f) else Slate900
                                    ),
                                    border = BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) WarningOrange else Slate700
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = r,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = if (isSelected) Color.White else Color(0xFFE2E8F0),
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = WarningOrange
                                            )
                                        }
                                    }
                                }
                            }

                            if (selectedRegion == "Custom Location") {
                                TacticalInputField(
                                    value = customRegionName,
                                    onValueChange = { customRegionName = it },
                                    label = "Enter Your District / City",
                                    placeholder = "e.g. Pune, Maharashtra",
                                    accentColor = WarningOrange
                                )
                            }
                        }

                        // -------------------------------------------------------------
                        // STEP 3: WATER
                        // -------------------------------------------------------------
                        3 -> {
                            StepHeader(
                                stepNumber = 3,
                                category = "WATER RESERVES",
                                title = "How much drinking water do you have?",
                                subtitle = "Survival baseline allocates 2.0 Litres per person per day for drinking and rehydration.",
                                icon = Icons.Default.WaterDrop,
                                accentColor = JeevanWaterBlue
                            )

                            // Main Drinking Water Field
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.5.dp, JeevanWaterBlue.copy(alpha = 0.6f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "DRINKING WATER CURRENTLY SEALED OR ACCESSIBLE",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = JeevanWaterBlue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        TacticalInputField(
                                            value = drinkingWaterInput,
                                            onValueChange = { input ->
                                                if (input.isEmpty() || input.matches(Regex("""^\d*\.?\d*$"""))) {
                                                    drinkingWaterInput = input
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            label = "Drinking Water",
                                            trailingText = "Litres",
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            accentColor = JeevanWaterBlue
                                        )
                                    }

                                    // Quick Presets
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf(0, 6, 12, 24, 48).forEach { qty ->
                                            OutlinedButton(
                                                onClick = { drinkingWaterInput = qty.toString() },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = if (drinkingWaterLiters.toInt() == qty) JeevanWaterBlue else Color(0xFFCBD5E1)
                                                ),
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (drinkingWaterLiters.toInt() == qty) JeevanWaterBlue else Slate700
                                                ),
                                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = if (qty == 0) "0 L" else "${qty}L",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Secondary Utility Water Field
                            TacticalInputField(
                                value = utilityWaterInput,
                                onValueChange = { input ->
                                    if (input.isEmpty() || input.matches(Regex("""^\d*\.?\d*$"""))) {
                                        utilityWaterInput = input
                                    }
                                },
                                label = "Utility / Sanitation Water (Optional)",
                                placeholder = "e.g. 20",
                                trailingText = "Litres",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                accentColor = JeevanWaterBlue
                            )

                            // Live Survival Estimate Banner
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (waterDaysRemaining < 1.0) EmergencyRed.copy(alpha = 0.15f) else JeevanGreenBg
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (waterDaysRemaining < 1.0) EmergencyRed else JeevanBrandGreen
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (waterDaysRemaining < 1.0) Icons.Default.Warning else Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = if (waterDaysRemaining < 1.0) EmergencyRed else JeevanBrandGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (waterDaysRemaining < 1.0) "CRITICAL SHORTAGE" else "SURVIVAL FORECAST",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = if (waterDaysRemaining < 1.0) EmergencyRed else JeevanBrandGreen,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }
                                    Text(
                                        text = if (drinkingWaterLiters <= 0.0) {
                                            "0.0 Litres entered. Zero days of drinking water available. Immediate dehydration hazard."
                                        } else {
                                            "For $totalPeople persons (${totalPeople * 2.0} L/day needed): ${String.format("%.1f", drinkingWaterLiters)} Litres will last ~${String.format("%.1f", waterDaysRemaining)} days."
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFF1F5F9))
                                    )
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // STEP 4: FOOD
                        // -------------------------------------------------------------
                        4 -> {
                            StepHeader(
                                stepNumber = 4,
                                category = "FOOD & RATIONS",
                                title = "How much food do you have?",
                                subtitle = "Count available meal portions. The survival engine estimates 2 meals per person per day.",
                                icon = Icons.Default.Restaurant,
                                accentColor = JeevanFoodYellow
                            )

                            // Non-perishable meals
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "NON-PERISHABLE / CANNED / DRY GOODS",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = JeevanFoodYellow,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Canned beans, pulses, dry rice, biscuits, sealed bars (lasts 6+ months).",
                                        style = MaterialTheme.typography.bodySmall.copy(color = JeevanTextMuted)
                                    )
                                    TacticalInputField(
                                        value = nonPerishableMealsInput,
                                        onValueChange = { input ->
                                            if (input.isEmpty() || input.matches(Regex("""^\d+$"""))) {
                                                nonPerishableMealsInput = input
                                            }
                                        },
                                        label = "Estimated Meals",
                                        trailingText = "meals",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        accentColor = JeevanFoodYellow
                                    )
                                }
                            }

                            // Perishable meals
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "PERISHABLE / FRESH / COOKED FOOD",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = CautionAmber,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Cooked meals, fresh produce, bread. Prioritize eating these in the first 24-48 hrs.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = JeevanTextMuted)
                                    )
                                    TacticalInputField(
                                        value = perishableMealsInput,
                                        onValueChange = { input ->
                                            if (input.isEmpty() || input.matches(Regex("""^\d+$"""))) {
                                                perishableMealsInput = input
                                            }
                                        },
                                        label = "Estimated Meals",
                                        trailingText = "meals",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        accentColor = CautionAmber
                                    )
                                }
                            }

                            // Zero food quick button
                            OutlinedButton(
                                onClick = {
                                    nonPerishableMealsInput = "0"
                                    perishableMealsInput = "0"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Slate700),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFCBD5E1))
                            ) {
                                Text("No Food Available (0 meals)", fontWeight = FontWeight.Medium)
                            }

                            // Live Food Estimate Banner
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (foodDaysRemaining < 1.0) EmergencyRed.copy(alpha = 0.15f) else Slate900
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (foodDaysRemaining < 1.0) EmergencyRed else JeevanFoodYellow
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "TOTAL PROVISIONS: $totalMeals MEALS",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = if (foodDaysRemaining < 1.0) EmergencyRed else JeevanFoodYellow,
                                            fontWeight = FontWeight.Black
                                        )
                                    )
                                    Text(
                                        text = if (totalMeals <= 0) {
                                            "0 meals entered. Zero days of food remaining. Urgent food procurement needed."
                                        } else {
                                            "For $totalPeople persons (${totalPeople * 2} meals/day needed): Provides ~${String.format("%.1f", foodDaysRemaining)} days of nutrition."
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFF1F5F9))
                                    )
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // STEP 5: POWER
                        // -------------------------------------------------------------
                        5 -> {
                            StepHeader(
                                stepNumber = 5,
                                category = "POWER & LIGHTING",
                                title = "What backup power do you have?",
                                subtitle = "Maintains communication, navigation, and emergency signaling when the power grid fails.",
                                icon = Icons.Default.ElectricBolt,
                                accentColor = JeevanBatteryAmber
                            )

                            // Phone battery level
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Phone Battery Level",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "$phoneBatteryPercent%",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = JeevanBatteryAmber,
                                                fontWeight = FontWeight.Black
                                            )
                                        )
                                    }
                                    Slider(
                                        value = phoneBatteryPercent.toFloat(),
                                        onValueChange = { phoneBatteryPercent = it.toInt() },
                                        valueRange = 0f..100f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = JeevanBatteryAmber,
                                            activeTrackColor = JeevanBatteryAmber,
                                            inactiveTrackColor = Slate700
                                        )
                                    )
                                }
                            }

                            // Power bank toggle
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "External Power Bank",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                text = if (hasPowerBank) "Available & functional" else "No backup battery",
                                                style = MaterialTheme.typography.bodySmall.copy(color = JeevanTextMuted)
                                            )
                                        }
                                        Switch(
                                            checked = hasPowerBank,
                                            onCheckedChange = { hasPowerBank = it },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = JeevanBrandGreen,
                                                uncheckedThumbColor = Slate700,
                                                uncheckedTrackColor = Slate800
                                            )
                                        )
                                    }

                                    if (hasPowerBank) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            listOf(5000, 10000, 20000, 30000).forEach { mah ->
                                                OutlinedButton(
                                                    onClick = { powerBankMah = mah },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    border = BorderStroke(
                                                        1.dp,
                                                        if (powerBankMah == mah) JeevanBrandGreen else Slate700
                                                    ),
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        contentColor = if (powerBankMah == mah) JeevanBrandGreen else Color(0xFFCBD5E1)
                                                    ),
                                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                                ) {
                                                    Text(
                                                        text = "${mah / 1000}k mAh",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Power Bank Charge: $powerBankPercent%",
                                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFCBD5E1))
                                            )
                                        }
                                        Slider(
                                            value = powerBankPercent.toFloat(),
                                            onValueChange = { powerBankPercent = it.toInt() },
                                            valueRange = 0f..100f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = JeevanBrandGreen,
                                                activeTrackColor = JeevanBrandGreen,
                                                inactiveTrackColor = Slate700
                                            )
                                        )
                                    }
                                }
                            }

                            // Flashlights and Spare Batteries
                            SetupStepperRow(
                                label = "Working Flashlights / Torches",
                                count = flashlightCount,
                                min = 0,
                                onIncrement = { flashlightCount++ },
                                onDecrement = { if (flashlightCount > 0) flashlightCount-- },
                                icon = Icons.Default.FlashlightOn,
                                accentColor = JeevanBatteryAmber
                            )

                            SetupStepperRow(
                                label = "Spare AA / AAA Batteries",
                                count = spareBatteriesCount,
                                min = 0,
                                onIncrement = { spareBatteriesCount += 2 },
                                onDecrement = { if (spareBatteriesCount >= 2) spareBatteriesCount -= 2 },
                                icon = Icons.Default.BatteryChargingFull,
                                accentColor = CautionAmber
                            )
                        }

                        // -------------------------------------------------------------
                        // STEP 6: FUEL & VEHICLE
                        // -------------------------------------------------------------
                        6 -> {
                            StepHeader(
                                stepNumber = 6,
                                category = "VEHICLE & EVACUATION FUEL",
                                title = "How much fuel do you currently have?",
                                subtitle = "Tracks whether your family can safely evacuate to distant shelters or high ground.",
                                icon = Icons.Default.DirectionsCar,
                                accentColor = JeevanFuelOrange
                            )

                            // Vehicle toggle
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Evacuation Vehicle Available?",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                text = if (hasVehicle) "Yes, motorized transport accessible" else "No vehicle (Foot evacuation only)",
                                                style = MaterialTheme.typography.bodySmall.copy(color = JeevanTextMuted)
                                            )
                                        }
                                        Switch(
                                            checked = hasVehicle,
                                            onCheckedChange = { hasVehicle = it },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = JeevanFuelOrange,
                                                uncheckedThumbColor = Slate700,
                                                uncheckedTrackColor = Slate800
                                            )
                                        )
                                    }

                                    if (hasVehicle) {
                                        Text(
                                            text = "SELECT VEHICLE TYPE",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = JeevanFuelOrange,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            vehicleTypes.forEach { vt ->
                                                val isSelected = selectedVehicleType == vt
                                                OutlinedButton(
                                                    onClick = { selectedVehicleType = vt },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    border = BorderStroke(
                                                        1.dp,
                                                        if (isSelected) JeevanFuelOrange else Slate700
                                                    ),
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        containerColor = if (isSelected) JeevanFuelOrange.copy(alpha = 0.2f) else Color.Transparent,
                                                        contentColor = if (isSelected) Color.White else Color(0xFFCBD5E1)
                                                    ),
                                                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp)
                                                ) {
                                                    Text(
                                                        text = vt.split(" / ")[0],
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Current Fuel Tank Level",
                                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                                            )
                                            Text(
                                                text = "$fuelPercent%",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    color = JeevanFuelOrange,
                                                    fontWeight = FontWeight.Black
                                                )
                                            )
                                        }
                                        Slider(
                                            value = fuelPercent.toFloat(),
                                            onValueChange = { fuelPercent = it.toInt() },
                                            valueRange = 0f..100f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = JeevanFuelOrange,
                                                activeTrackColor = JeevanFuelOrange,
                                                inactiveTrackColor = Slate700
                                            )
                                        )
                                    }
                                }
                            }

                            // Evacuation Range Preview
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = if (hasVehicle) "ESTIMATED VEHICLE EVACUATION RANGE" else "EVACUATION STRATEGY",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = JeevanFuelOrange,
                                            fontWeight = FontWeight.Black
                                        )
                                    )
                                    Text(
                                        text = if (hasVehicle) {
                                            "With $fuelPercent% tank: Estimated ~${estimatedRangeKm.toInt()} km highway range in $selectedVehicleType."
                                        } else {
                                            "No motorized transport available. Jeevan Setu will prioritize walking routes and local perimeter safe shelters."
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFF1F5F9))
                                    )
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // STEP 7: MEDICAL SUPPLIES
                        // -------------------------------------------------------------
                        7 -> {
                            StepHeader(
                                stepNumber = 7,
                                category = "MEDICAL SUPPLIES",
                                title = "Do you have first-aid or critical daily medicines?",
                                subtitle = "Identifies medical vulnerabilities and supply depletion timelines before disaster onset.",
                                icon = Icons.Default.Medication,
                                accentColor = JeevanMedicalRed
                            )

                            // First Aid Kit toggle
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Basic First Aid Kit Available?",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "Antiseptic, bandages, sterile gauze, burn dressing, ORS packets.",
                                            style = MaterialTheme.typography.bodySmall.copy(color = JeevanTextMuted)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Switch(
                                        checked = hasFirstAidKit,
                                        onCheckedChange = { hasFirstAidKit = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = JeevanBrandGreen,
                                            uncheckedThumbColor = Slate700,
                                            uncheckedTrackColor = Slate800
                                        )
                                    )
                                }
                            }

                            // Critical Prescription Toggle & Input
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Critical Daily Prescription Medicine?",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                text = "Insulin, hypertension, heart medication, asthma inhalers, etc.",
                                                style = MaterialTheme.typography.bodySmall.copy(color = JeevanTextMuted)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Switch(
                                            checked = hasCriticalPrescription,
                                            onCheckedChange = { hasCriticalPrescription = it },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = JeevanMedicalRed,
                                                uncheckedThumbColor = Slate700,
                                                uncheckedTrackColor = Slate800
                                            )
                                        )
                                    }

                                    if (hasCriticalPrescription) {
                                        TacticalInputField(
                                            value = medName,
                                            onValueChange = { medName = it },
                                            label = "Medication Name",
                                            placeholder = "e.g. Amlodipine 5mg or Insulin",
                                            accentColor = JeevanMedicalRed
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            TacticalInputField(
                                                value = medQtyInput,
                                                onValueChange = { input ->
                                                    if (input.isEmpty() || input.matches(Regex("""^\d+$"""))) {
                                                        medQtyInput = input
                                                    }
                                                },
                                                modifier = Modifier.weight(1f),
                                                label = "Total Doses / Pills",
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                accentColor = JeevanMedicalRed
                                            )

                                            TacticalInputField(
                                                value = medDailyInput,
                                                onValueChange = { input ->
                                                    if (input.isEmpty() || input.matches(Regex("""^\d+$"""))) {
                                                        medDailyInput = input
                                                    }
                                                },
                                                modifier = Modifier.weight(1f),
                                                label = "Doses Per Day",
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                                accentColor = JeevanMedicalRed
                                            )
                                        }

                                        if (medName.isNotBlank() && medQty > 0) {
                                            Text(
                                                text = "🛡️ Prescription will last for approximately $medDaysRemaining days.",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = JeevanBrandGreen,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // -------------------------------------------------------------
                        // STEP 8: EMERGENCY EQUIPMENT
                        // -------------------------------------------------------------
                        8 -> {
                            StepHeader(
                                stepNumber = 8,
                                category = "EMERGENCY EQUIPMENT",
                                title = "What emergency equipment is packed?",
                                subtitle = "Pre-configures your offline preparedness checklist so search & rescue know your survival status.",
                                icon = Icons.Default.Build,
                                accentColor = JeevanEquipmentCyan
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Slate900),
                                border = BorderStroke(1.dp, Slate700)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    gearState.value.forEach { (title, isChecked) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable {
                                                    val updated = gearState.value.toMutableMap()
                                                    updated[title] = !isChecked
                                                    gearState.value = updated
                                                }
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = { checked ->
                                                    val updated = gearState.value.toMutableMap()
                                                    updated[title] = checked
                                                    gearState.value = updated
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = JeevanEquipmentCyan,
                                                    checkmarkColor = Slate950,
                                                    uncheckedColor = Slate700
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = if (isChecked) Color.White else Color(0xFF94A3B8),
                                                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            // Final Setup Summary Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = JeevanGreenBg),
                                border = BorderStroke(1.dp, JeevanGreenBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "✓ READY FOR OFFLINE ACTIVATION",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = JeevanBrandGreen,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Text(
                                        text = "• Household: $totalPeople persons (${adults} adults, ${children} children, ${elderly} elderly)",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                    )
                                    Text(
                                        text = "• Water: ${String.format("%.1f", drinkingWaterLiters)} L (~${String.format("%.1f", waterDaysRemaining)} days)",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                    )
                                    Text(
                                        text = "• Food: $totalMeals meals (~${String.format("%.1f", foodDaysRemaining)} days)",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                    )
                                    Text(
                                        text = "• Power: $phoneBatteryPercent% battery" + if (hasPowerBank) " + ${powerBankMah / 1000}k mAh power bank" else "",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                    )
                                    Text(
                                        text = "• Vehicle: " + if (hasVehicle) "$selectedVehicleType ($fuelPercent% fuel)" else "None (Foot)",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                                    )
                                }
                            }

                            SafetyDisclaimerCard()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Navigation Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Slate700),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("← BACK", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Button(
                    onClick = {
                        if (currentStep < totalSteps) {
                            currentStep++
                        } else {
                            isSaving = true
                            val finalRegion = if (selectedRegion == "Custom Location" && customRegionName.isNotBlank()) {
                                customRegionName.trim()
                            } else {
                                selectedRegion
                            }

                            val completedGearKeywords = gearState.value
                                .filter { it.value }
                                .map { it.key }

                            viewModel.saveOnboardingData(
                                adults = adults,
                                children = children,
                                elderly = elderly,
                                injured = injured,
                                specialNeeds = specialNeeds,
                                regionName = finalRegion,
                                drinkingWaterLiters = drinkingWaterLiters,
                                utilityWaterLiters = utilityWaterLiters,
                                perishableMeals = perishableMeals,
                                nonPerishableMeals = nonPerishableMeals,
                                phoneBatteryPercent = phoneBatteryPercent,
                                hasPowerBank = hasPowerBank,
                                powerBankMah = powerBankMah,
                                powerBankPercent = powerBankPercent,
                                flashlightCount = flashlightCount,
                                spareBatteriesCount = spareBatteriesCount,
                                hasVehicle = hasVehicle,
                                vehicleType = selectedVehicleType,
                                fuelPercent = fuelPercent,
                                hasFirstAidKit = hasFirstAidKit,
                                hasCriticalMed = hasCriticalPrescription,
                                criticalMedName = medName,
                                criticalMedQty = medQty,
                                criticalMedDailyUsage = medDaily,
                                equipmentCompletedKeywords = completedGearKeywords,
                                onComplete = {
                                    isSaving = false
                                    onComplete()
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(if (currentStep > 1) 1.5f else 1f)
                        .height(50.dp)
                        .testTag(if (currentStep < totalSteps) "onboarding_continue_button" else "onboarding_finish_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStep == totalSteps) JeevanBrandGreen else WarningOrange
                    ),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (currentStep < totalSteps) "CONTINUE →" else "SAVE & ENTER JEEVANSETU",
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepHeader(
    stepNumber: Int,
    category: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = JeevanTextMuted,
                lineHeight = 18.sp
            )
        )
    }
}

@Composable
private fun SetupStepperRow(
    label: String,
    count: Int,
    min: Int = 0,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.dp, Slate700)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDecrement,
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(1.dp, Slate700),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    enabled = count > min
                ) {
                    Text("-", fontWeight = FontWeight.Black, fontSize = 18.sp, color = if (count > min) Color.White else Slate700)
                }

                Text(
                    text = "$count",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Button(
                    onClick = onIncrement,
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("+", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Slate950)
                }
            }
        }
    }
}
