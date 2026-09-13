package com.example.domain.model

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val stateRegion: String,
    val flagEmoji: String,
    val greetingPreview: String
) {
    ENGLISH(
        code = "en",
        nativeName = "English",
        englishName = "English",
        stateRegion = "National & International",
        flagEmoji = "🌐",
        greetingPreview = "Welcome to JeevanSetu Disaster Guard"
    ),

    HINDI(
        code = "hi",
        nativeName = "हिन्दी",
        englishName = "Hindi",
        stateRegion = "राष्ट्रीय (National)",
        flagEmoji = "🇮🇳",
        greetingPreview = "जीवनसेतु आपदा गार्ड में आपका स्वागत है"
    );

    val displayName: String
        get() = englishName

    val locale: Locale
        get() = when (this) {
            ENGLISH -> Locale.ENGLISH
            HINDI -> Locale("hi", "IN")
        }
}