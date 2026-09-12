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
    ),
    KANNADA(
        code = "kn",
        nativeName = "ಕನ್ನಡ",
        englishName = "Kannada",
        stateRegion = "ಕರ್ನಾಟಕ (Karnataka)",
        flagEmoji = "🟡",
        greetingPreview = "ಜೀವನಸೇತು ವಿಪತ್ತು ರಕ್ಷಣೆಗೆ ಸುಸ್ವಾಗತ"
    ),
    TELUGU(
        code = "te",
        nativeName = "తెలుగు",
        englishName = "Telugu",
        stateRegion = "ఆంధ్రప్రదేశ్ & తెలంగాణ",
        flagEmoji = "🔵",
        greetingPreview = "జీవనసేతు విపత్తు రక్షణకు స్వాగతం"
    ),
    TAMIL(
        code = "ta",
        nativeName = "தமிழ்",
        englishName = "Tamil",
        stateRegion = "தமிழ்நாடு (Tamil Nadu)",
        flagEmoji = "🔴",
        greetingPreview = "ஜீவன்சேது பேரிடர் காப்பகத்திற்கு நல்வரவு"
    );

    val displayName: String get() = englishName

    val locale: Locale
        get() = when (this) {
            ENGLISH -> Locale.ENGLISH
            HINDI -> Locale("hi", "IN")
            KANNADA -> Locale("kn", "IN")
            TELUGU -> Locale("te", "IN")
            TAMIL -> Locale("ta", "IN")
        }
}
