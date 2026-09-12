package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// MINT BREEZE DESIGN SYSTEM PALETTE
// A calm, trustworthy, light-first emergency preparedness visual identity
// ============================================================================

// Primary Mint Palette
val MintPrimary = Color(0xFF2DBE9B)
val MintDeep = Color(0xFF168F78)
val MintLight = Color(0xFFDDF7EF)
val MintVeryLight = Color(0xFFF1FBF7)

// Surfaces & Backgrounds
val AppBackground = Color(0xFFF7FAF9)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceSubtle = Color(0xFFF0F5F3)
val BorderSubtle = Color(0xFFDCE8E4)
val BorderLight = Color(0xFFEAF1EE)

// Typography Colors
val TextPrimary = Color(0xFF172522)
val TextSecondary = Color(0xFF61716D)
val TextTertiary = Color(0xFF8A9A96)

// Semantic Emergency & Status Colors (Calm, accessible, non-neon)
val StatusSuccess = Color(0xFF2E9B6F)
val StatusSuccessBg = Color(0xFFEBF7F2)
val StatusSuccessBorder = Color(0xFFBCE3D3)

val StatusWarning = Color(0xFFD99A32)
val StatusWarningBg = Color(0xFFFEF8EE)
val StatusWarningBorder = Color(0xFFF6DEB8)

val StatusElevated = Color(0xFFE07238)
val StatusElevatedBg = Color(0xFFFFF4EE)
val StatusElevatedBorder = Color(0xFFFCD3BD)

val StatusDanger = Color(0xFFD95C5C)
val StatusDangerBg = Color(0xFFFDF1F1)
val StatusDangerBorder = Color(0xFFF8C8C8)

val StatusCritical = Color(0xFFC53939)
val StatusCriticalBg = Color(0xFFFBEAEA)
val StatusCriticalBorder = Color(0xFFF5B6B6)

val StatusInfo = Color(0xFF4D8FD8)
val StatusInfoBg = Color(0xFFEFF5FC)
val StatusInfoBorder = Color(0xFFC2DCF5)

// Disaster Alert & Status Colors (backward compatibility aliases)
val EmergencyRed = StatusDanger
val EmergencyRedDark = StatusCritical
val WarningOrange = StatusElevated
val CautionAmber = StatusWarning
val SafetyGreen = StatusSuccess
val SafetyGreenDark = MintDeep
val RescueCyan = StatusInfo

// Light Surface Slate Palette
val Slate50 = Color(0xFFF8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate300 = Color(0xFFCBD5E1)
val Slate600 = Color(0xFF475569)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate850 = Color(0xFF131D31)
val Slate900 = Color(0xFF0F172A)
val Slate950 = Color(0xFF0B1120)

// Jeevan Setu Legacy Aliases (mapped to Mint Breeze Light Tokens)
val JeevanBg = AppBackground
val JeevanSurface = SurfaceWhite
val JeevanCard = SurfaceWhite
val JeevanCardBorder = BorderSubtle
val JeevanBrandGreen = MintDeep
val JeevanBrandGreenBg = MintLight
val JeevanBrandGreenBorder = MintPrimary
val JeevanGreenBg = MintVeryLight
val JeevanGreenBorder = MintPrimary
val JeevanRedBg = StatusDangerBg
val JeevanRedBorder = StatusDanger
val JeevanWaterBlue = Color(0xFF3B99DC)
val JeevanFoodYellow = Color(0xFFD99A32)
val JeevanBatteryAmber = Color(0xFFE07238)
val JeevanFuelOrange = Color(0xFFDE6B35)
val JeevanMedicalRed = StatusDanger
val JeevanEquipmentCyan = MintDeep
val JeevanTextMuted = TextSecondary
val JeevanNavBg = SurfaceWhite
