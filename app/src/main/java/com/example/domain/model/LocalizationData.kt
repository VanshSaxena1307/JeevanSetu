package com.example.domain.model

data class AppStrings(
    val languageName: String,
    val locationTitle: String,
    val locationSubtitle: String,
    val searchPlaceholder: String,
    val alertsFoundText: String,
    val satelliteConnectPrompt: String,
    val satelliteConnectedPrompt: String,
    val satelliteSubActive: String,
    val satelliteSubStandby: String,
    val disasterGuide: String,
    val disasterGuideSub: String,
    val weatherForecast: String,
    val weatherForecastSub: String,
    val uvLabel: String,
    val humidityLabel: String,
    val windLabel: String,
    val aqiLabel: String,
    val mapTitle: String,
    val downloadAreaMap: String,
    val tacticalVectorLayer: String,
    val offlineBadge: String,
    val openMap: String,
    val sectorName: String,
    val activeAlertsTitle: String,
    val share: String,
    val listen: String,
    val playing: String,
    val stopAudio: String,
    val dosAndDonts: String,
    val dosTitle: String,
    val dontsTitle: String,
    val safetyProtocols: String,
    val close: String,
    val weatherRadarTitle: String,
    val realtimeBarometer: String,
    val surfaceReadings: String,
    val pressureTrend: String,
    val partlyCloudy: String,
    val emergencySuite: String,
    val toolMaps: String,
    val toolShelters: String,
    val toolContacts: String,
    val toolAssessment: String,
    val toolCompass: String,
    val toolFirstAid: String,
    val toolCalculator: String,
    val toolChecklist: String,
    val selectLanguageTitle: String,
    val selectLanguageSubtitle: String,
    val voiceSupportedBadge: String,
    val changeLanguageSuccess: String
)

object LocalizationData {

    fun getStrings(language: AppLanguage): AppStrings {
        return when (language) {
            AppLanguage.ENGLISH -> AppStrings(
                languageName = "English",
                locationTitle = "Bengaluru",
                locationSubtitle = "Neeladri Road, Electronic City",
                searchPlaceholder = "Search location, alerts, guidelines...",
                alertsFoundText = "active emergency alerts",
                satelliteConnectPrompt = "Connect to satellite receiver",
                satelliteConnectedPrompt = "Connected to satellite receiver",
                satelliteSubActive = "12 Ch NavIC Constellation • -82 dBm Live",
                satelliteSubStandby = "Standby • Tap to pair satellite transceiver",
                disasterGuide = "Disaster Guide",
                disasterGuideSub = "Emergency protocols & offline steps",
                weatherForecast = "Weather Forecast",
                weatherForecastSub = "Barometer & radar telemetry",
                uvLabel = "UV Index",
                humidityLabel = "Humidity",
                windLabel = "Wind Speed",
                aqiLabel = "Air Quality",
                mapTitle = "Map",
                downloadAreaMap = "Download Area Map →",
                tacticalVectorLayer = "Tactical offline vector layer • Download local area map",
                offlineBadge = "100% OFFLINE",
                openMap = "Open",
                sectorName = "KARNATAKA SECTOR",
                activeAlertsTitle = "Active Alerts",
                share = "Share",
                listen = "Listen",
                playing = "Playing...",
                stopAudio = "Stop Audio",
                dosAndDonts = "Dos & Don't",
                dosTitle = "DOs (Recommended Actions)",
                dontsTitle = "DON'Ts (Hazardous Actions)",
                safetyProtocols = "Safety Protocols & Verification",
                close = "Close",
                weatherRadarTitle = "Weather & Atmospheric Radar",
                realtimeBarometer = "Real-time Offline Barometer",
                surfaceReadings = "SURFACE & SENSOR READINGS",
                pressureTrend = "Pressure Trend: Steady (1012.8 hPa)",
                partlyCloudy = "Partly Cloudy with Cyclonic Inflow",
                emergencySuite = "OFFLINE EMERGENCY SUITE",
                toolMaps = "Offline Maps & GPS",
                toolShelters = "Safe Shelters",
                toolContacts = "Emergency Contacts",
                toolAssessment = "Risk Assessment",
                toolCompass = "Evacuation Compass",
                toolFirstAid = "First Aid Manual",
                toolCalculator = "Survival Calculator",
                toolChecklist = "Emergency Checklist",
                selectLanguageTitle = "Choose Emergency Language",
                selectLanguageSubtitle = "All alerts, audio readouts, sensor telemetry, and safety guides adapt instantly to your selected language.",
                voiceSupportedBadge = "Audio Readout Supported",
                changeLanguageSuccess = "Language switched to English"
            )

            AppLanguage.HINDI -> AppStrings(
                languageName = "हिन्दी",
                locationTitle = "बेंगलुरु",
                locationSubtitle = "नीलाद्री रोड, इलेक्ट्रॉनिक सिटी",
                searchPlaceholder = "स्थान, अलर्ट, सुरक्षा नियम खोजें...",
                alertsFoundText = "सक्रिय आपातकालीन अलर्ट",
                satelliteConnectPrompt = "उपग्रह रिसीवर से कनेक्ट करें",
                satelliteConnectedPrompt = "उपग्रह रिसीवर से कनेक्टेड",
                satelliteSubActive = "12 Ch NavIC नक्षत्र • -82 dBm सक्रिय",
                satelliteSubStandby = "स्टैंडबाय • सैटेलाइट ट्रांससीवर लिंक करने हेतु टैप करें",
                disasterGuide = "आपदा गाइड",
                disasterGuideSub = "आपातकालीन प्रोटोकॉल और ऑफलाइन चरण",
                weatherForecast = "मौसम पूर्वानुमान",
                weatherForecastSub = "बैरोमीटर और रडार टेलीमेट्री",
                uvLabel = "यूवी इंडेक्स",
                humidityLabel = "आर्द्रता",
                windLabel = "हवा की गति",
                aqiLabel = "वायु गुणवत्ता",
                mapTitle = "मानचित्र",
                downloadAreaMap = "क्षेत्रीय नक्शा डाउनलोड करें →",
                tacticalVectorLayer = "सामरिक ऑफलाइन वेक्टर परत • स्थानीय नक्शा डाउनलोड करें",
                offlineBadge = "100% ऑफलाइन",
                openMap = "खोलें",
                sectorName = "कर्नाटक सेक्टर",
                activeAlertsTitle = "सक्रिय अलर्ट",
                share = "साझा करें",
                listen = "सुनें",
                playing = "चल रहा है...",
                stopAudio = "ऑडियो रोकें",
                dosAndDonts = "क्या करें / क्या न करें",
                dosTitle = "क्या करें (अनुशंसित सुरक्षा कार्य)",
                dontsTitle = "क्या न करें (खतरनाक कार्य)",
                safetyProtocols = "सुरक्षा प्रोटोकॉल और सत्यापन",
                close = "बंद करें",
                weatherRadarTitle = "मौसम और वायुमंडलीय रडार",
                realtimeBarometer = "वास्तविक समय ऑफलाइन बैरोमीटर",
                surfaceReadings = "सतह और सेंसर रीडिंग",
                pressureTrend = "दबाव प्रवृत्ति: स्थिर (1012.8 hPa)",
                partlyCloudy = "आंशिक रूप से बादल, चक्रवाती प्रवाह के साथ",
                emergencySuite = "ऑफलाइन आपातकालीन सुइट",
                toolMaps = "ऑफलाइन नक्शे और जीपीएस",
                toolShelters = "सुरक्षित आश्रय स्थल",
                toolContacts = "आपातकालीन संपर्क",
                toolAssessment = "जोखिम मूल्यांकन",
                toolCompass = "निकासी कम्पास",
                toolFirstAid = "प्राथमिक चिकित्सा नियमावली",
                toolCalculator = "उत्तरजीविता कैलकुलेटर",
                toolChecklist = "आपातकालीन चेकलिस्ट",
                selectLanguageTitle = "आपातकालीन भाषा चुनें",
                selectLanguageSubtitle = "सभी अलर्ट, ऑडियो प्रसारण, सेंसर रीडिंग और सुरक्षा निर्देश तुरंत चुनी गई भाषा में बदल जाएंगे।",
                voiceSupportedBadge = "वॉयस प्रसारण समर्थित",
                changeLanguageSuccess = "भाषा बदलकर हिन्दी कर दी गई है"
            )

            AppLanguage.KANNADA -> AppStrings(
                languageName = "ಕನ್ನಡ",
                locationTitle = "ಬೆಂಗಳೂರು",
                locationSubtitle = "ನೀಲಾದ್ರಿ ರಸ್ತೆ, ಇಲೆಕ್ಟ್ರಾನಿಕ್ ಸಿಟಿ",
                searchPlaceholder = "ಸ್ಥಳ, ಎಚ್ಚರಿಕೆಗಳು, ಮಾರ್ಗಸೂಚಿ ಹುಡುಕಿ...",
                alertsFoundText = "ಸಕ್ರಿಯ ತುರ್ತು ಎಚ್ಚರಿಕೆಗಳು",
                satelliteConnectPrompt = "ಉಪಗ್ರಹ ರಿಸೀವರ್‌ಗೆ ಸಂಪರ್ಕಿಸಿ",
                satelliteConnectedPrompt = "ಉಪಗ್ರಹ ರಿಸೀವರ್‌ಗೆ ಸಂಪರ್ಕಗೊಂಡಿದೆ",
                satelliteSubActive = "12 Ch NavIC ಉಪಗ್ರಹ ಸಂಪರ್ಕ • -82 dBm ಲೈವ್",
                satelliteSubStandby = "ಸ್ಟ್ಯಾಂಡ್‌ಬೈ • ಉಪಗ್ರಹ ಲಿಂಕ್ ಮಾಡಲು ಟ್ಯಾಪ್ ಮಾಡಿ",
                disasterGuide = "ವಿಪತ್ತು ಮಾರ್ಗದರ್ಶಿ",
                disasterGuideSub = "ತುರ್ತು ಪ್ರೋಟೋಕಾಲ್‌ಗಳು ಮತ್ತು ಆಫ್‌ಲೈನ್ ಹಂತಗಳು",
                weatherForecast = "ಹವಾಮಾನ ಮುನ್ಸೂಚನೆ",
                weatherForecastSub = "ಬ್ಯಾರೊಮೀಟರ್ ಮತ್ತು ರೇಡಾರ್ ಟೆಲಿಮೆಟ್ರಿ",
                uvLabel = "ಯುವಿ ಸೂಚ್ಯಂಕ",
                humidityLabel = "ತೇವಾಂಶ",
                windLabel = "ಗಾಳಿಯ ವೇಗ",
                aqiLabel = "ವಾಯು ಗುಣಮಟ್ಟ",
                mapTitle = "ನಕ್ಷೆ",
                downloadAreaMap = "ಪ್ರದೇಶದ ನಕ್ಷೆ ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ →",
                tacticalVectorLayer = "ಆಫ್‌ಲೈನ್ ವೆಕ್ಟರ್ ಪದರ • ಸ್ಥಳೀಯ ನಕ್ಷೆಯನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ",
                offlineBadge = "100% ಆಫ್‌ಲೈನ್",
                openMap = "ತೆರೆಯಿರಿ",
                sectorName = "ಕರ್ನಾಟಕ ವಲಯ",
                activeAlertsTitle = "ಸಕ್ರಿಯ ಎಚ್ಚರಿಕೆಗಳು",
                share = "ಹಂಚಿಕೊಳ್ಳಿ",
                listen = "ಕೇಳಿ",
                playing = "ಪ್ಲೇ ಆಗುತ್ತಿದೆ...",
                stopAudio = "ಆಡಿಯೋ ನಿಲ್ಲಿಸಿ",
                dosAndDonts = "ಮಾಡಬೇಕಾದುದು / ಮಾಡಬಾರದು",
                dosTitle = "ಮಾಡಬೇಕಾದುದು (ಶಿಫಾರಸು ಮಾಡಿದ ಕ್ರಮಗಳು)",
                dontsTitle = "ಮಾಡಬಾರದು (ಅಪಾಯಕಾರಿ ಕ್ರಮಗಳು)",
                safetyProtocols = "ಸುರಕ್ಷತಾ ಪ್ರೋಟೋಕಾಲ್‌ಗಳು ಮತ್ತು ಪರಿಶೀಲನೆ",
                close = "ಮುಚ್ಚಿ",
                weatherRadarTitle = "ಹವಾಮಾನ ಮತ್ತು ವಾತಾವರಣದ ರೇಡಾರ್",
                realtimeBarometer = "ನೈಜ-ಸಮಯದ ಆಫ್‌ಲೈನ್ ಬ್ಯಾರೊಮೀಟರ್",
                surfaceReadings = "ಮೇಲ್ಮೈ ಮತ್ತು ಸೆನ್ಸರ್ ಮಾಪನಗಳು",
                pressureTrend = "ಒತ್ತಡದ ಪ್ರವೃತ್ತಿ: ಸ್ಥಿರ (1012.8 hPa)",
                partlyCloudy = "ಭಾಗಶಃ ಮೋಡ ಕವಿದ ವಾತಾವರಣ, ಚಂಡಮಾರುತ ಪ್ರಭಾವ",
                emergencySuite = "ಆಫ್‌ಲೈನ್ ತುರ್ತು ಸೂಟ್",
                toolMaps = "ಆಫ್‌ಲೈನ್ ನಕ್ಷೆಗಳು ಮತ್ತು ಜಿಪಿಎಸ್",
                toolShelters = "ಸುರಕ್ಷಿತ ಆಶ್ರಯಗಳು",
                toolContacts = "ತುರ್ತು ಸಂಪರ್ಕಗಳು",
                toolAssessment = "ಅಪಾಯದ ಮೌಲ್ಯಮಾಪನ",
                toolCompass = "ಸ್ಥಳಾಂತರಿಸುವ ದಿಕ್ಸೂಚಿ",
                toolFirstAid = "ಪ್ರಥಮ ಚಿಕಿತ್ಸಾ ಕೈಪಿಡಿ",
                toolCalculator = "ಬದುಕುಳಿಯುವಿಕೆಯ ಕ್ಯಾಲ್ಕುಲೇಟರ್",
                toolChecklist = "ತುರ್ತು ಪರಿಶೀಲನಾಪಟ್ಟಿ",
                selectLanguageTitle = "ತುರ್ತು ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ",
                selectLanguageSubtitle = "ಎಲ್ಲಾ ಎಚ್ಚರಿಕೆಗಳು, ಧ್ವನಿ ಓದುವಿಕೆ, ಸಂವೇದಕ ಟೆಲಿಮೆಟ್ರಿ ಮತ್ತು ಮಾರ್ಗದರ್ಶಿಗಳು ತಕ್ಷಣ ನಿಮ್ಮ ಭಾಷೆಗೆ ಹೊಂದಿಕೊಳ್ಳುತ್ತವೆ.",
                voiceSupportedBadge = "ಧ್ವನಿ ಓದುವಿಕೆ ಸಕ್ರಿಯವಾಗಿದೆ",
                changeLanguageSuccess = "ಭಾಷೆಯನ್ನು ಕನ್ನಡಕ್ಕೆ ಬದಲಾಯಿಸಲಾಗಿದೆ"
            )

            AppLanguage.TELUGU -> AppStrings(
                languageName = "తెలుగు",
                locationTitle = "బెంగళూరు",
                locationSubtitle = "నీలాద్రి రోడ్, ఎలక్ట్రానిక్ సిటీ",
                searchPlaceholder = "స్థానం, హెచ్చరికలు, మార్గదర్శకాలను శోధించండి...",
                alertsFoundText = "క్రియాశీల అత్యవసర హెచ్చరికలు",
                satelliteConnectPrompt = "ఉపగ్రహ రిసీవర్‌కు కనెక్ట్ చేయండి",
                satelliteConnectedPrompt = "ఉపగ్రహ రిసీవర్‌కు కనెక్ట్ చేయబడింది",
                satelliteSubActive = "12 Ch NavIC ఉపగ్రహ లింక్ • -82 dBm లైవ్",
                satelliteSubStandby = "స్టాండ్‌బై • శాటిలైట్ ట్రాన్సీవర్ జత చేయడానికి నొక్కండి",
                disasterGuide = "విపత్తు గైడ్",
                disasterGuideSub = "అత్యవసర ప్రోటోకాల్‌లు & ఆఫ్‌లైన్ దశలు",
                weatherForecast = "వాతావరణ సూచన",
                weatherForecastSub = "బారోమీటర్ & రాడార్ టెలిమెట్రీ",
                uvLabel = "UV సూచిక",
                humidityLabel = "తేమ",
                windLabel = "గాలి వేగం",
                aqiLabel = "గాలి నాణ్యత",
                mapTitle = "మ్యాప్",
                downloadAreaMap = "ప్రాంత మ్యాప్‌ను డౌన్‌లోడ్ చేయండి →",
                tacticalVectorLayer = "ఆఫ్‌లైన్ వెక్టర్ లేయర్ • స్థానిక మ్యాప్‌ను డౌన్‌లోడ్ చేయండి",
                offlineBadge = "100% ఆఫ్‌లైన్",
                openMap = "తెరవండి",
                sectorName = "కర్ణాటక సెక్టార్",
                activeAlertsTitle = "క్రియాశీల హెచ్చరికలు",
                share = "భాగస్వామ్యం",
                listen = "వినండి",
                playing = "ప్లే అవుతోంది...",
                stopAudio = "ఆడియో ఆపండి",
                dosAndDonts = "చేయవలసినవి / చేయకూడనివి",
                dosTitle = "చేయవలసినవి (రక్షణ చర్యలు)",
                dontsTitle = "చేయకూడనివి (ప్రమాదకర చర్యలు)",
                safetyProtocols = "భద్రతా ప్రోటోకాల్‌లు & ధృవీకరణ",
                close = "మూసివేయి",
                weatherRadarTitle = "వాతావరణ & వాతావరణ రాడార్",
                realtimeBarometer = "రియల్-టైమ్ ఆఫ్‌లైన్ బారోమీటర్",
                surfaceReadings = "ఉపరితల & సెన్సార్ రీడింగ్‌లు",
                pressureTrend = "పీడన ధోరణి: స్థిరంగా (1012.8 hPa)",
                partlyCloudy = "తుఫాను ప్రభావంతో పాక్షికంగా మేఘావృతం",
                emergencySuite = "ఆఫ్‌లైన్ అత్యవసర సూట్",
                toolMaps = "ఆఫ్‌లైన్ మ్యాప్‌లు & జీపీఎస్",
                toolShelters = "రక్షిత ఆశ్రయాలు",
                toolContacts = "అత్యవసర పరిచయాలు",
                toolAssessment = "ప్రమాద అంచనా",
                toolCompass = "తరలింపు దిక్సూచి",
                toolFirstAid = "ప్రథమ చికిత్స మాన్యువల్",
                toolCalculator = "మనుగడ కాలిక్యులేటర్",
                toolChecklist = "అత్యవసర చెక్‌లిస్ట్",
                selectLanguageTitle = "అత్యవసర భాషను ఎంచుకోండి",
                selectLanguageSubtitle = "అన్ని హెచ్చరికలు, ఆడియో చదువులు, సెన్సార్ సమాచారం మీ ఎంచుకున్న భాషలోకి తక్షణమే మారతాయి.",
                voiceSupportedBadge = "వాయిస్ రీడౌట్ మద్దతు ఉంది",
                changeLanguageSuccess = "భాష తెలుగులోకి మార్చబడింది"
            )

            AppLanguage.TAMIL -> AppStrings(
                languageName = "தமிழ்",
                locationTitle = "பெங்களூரு",
                locationSubtitle = "நீலாத்ரி சாலை, எலக்ட்ரானிக் சிட்டி",
                searchPlaceholder = "இடம், எச்சரிக்கைகள், வழிகாட்டுதல்களைத் தேடுங்கள்...",
                alertsFoundText = "செயலில் உள்ள அவசர எச்சரிக்கைகள்",
                satelliteConnectPrompt = "செயற்கைக்கோள் ரிசீவருடன் இணைக்கவும்",
                satelliteConnectedPrompt = "செயற்கைக்கோள் ரிசீவருடன் இணைக்கப்பட்டது",
                satelliteSubActive = "12 Ch NavIC செயற்கைக்கோள் இணைப்பு • -82 dBm நேரலை",
                satelliteSubStandby = "காத்திருப்பு • செயற்கைக்கோள் இணைக்க தட்டவும்",
                disasterGuide = "பேரிடர் வழிகாட்டி",
                disasterGuideSub = "அவசர நெறிமுறைகள் & ஆஃப்லைன் படிகள்",
                weatherForecast = "வானிலை முன்னறிவிப்பு",
                weatherForecastSub = "பாரோமீட்டர் & ரேடார் டெலிமெட்ரி",
                uvLabel = "UV குறியீடு",
                humidityLabel = "ஈரப்பதம்",
                windLabel = "காற்றின் வேகம்",
                aqiLabel = "காற்று தரம்",
                mapTitle = "வரைபடம்",
                downloadAreaMap = "பகுதி வரைபடத்தைப் பதிவிறக்கவும் →",
                tacticalVectorLayer = "ஆஃப்லைன் வெக்டார் அடுக்கு • உள்ளூர் வரைபடத்தைப் பதிவிறக்கவும்",
                offlineBadge = "100% ஆஃப்லைன்",
                openMap = "திற",
                sectorName = "கர்நாடகா மண்டலம்",
                activeAlertsTitle = "செயலில் உள்ள எச்சரிக்கைகள்",
                share = "பகிர்",
                listen = "கேளுங்கள்",
                playing = "ஒலிக்கிறது...",
                stopAudio = "ஆடியோவை நிறுத்து",
                dosAndDonts = "செய்ய வேண்டியவை / செய்யக்கூடாதவை",
                dosTitle = "செய்ய வேண்டியவை (பாதுகாப்பு நடவடிக்கைகள்)",
                dontsTitle = "செய்யக்கூடாதவை (ஆபத்தான செயல்கள்)",
                safetyProtocols = "பாதுகாப்பு நெறிமுறைகள் & சரிபார்ப்பு",
                close = "மூடு",
                weatherRadarTitle = "வானிலை & வளிமண்டல ரேடார்",
                realtimeBarometer = "நிகழ்நேர ஆஃப்லைன் பாரோமீட்டர்",
                surfaceReadings = "மேற்பரப்பு & சென்சார் அளவீடுகள்",
                pressureTrend = "அழுத்த போக்கு: நிலையானது (1012.8 hPa)",
                partlyCloudy = "புயல் காற்றுடன் ஓரளவு மேகமூட்டம்",
                emergencySuite = "ஆஃப்லைன் அவசர தொகுப்பு",
                toolMaps = "ஆஃப்லைன் வரைபடங்கள் & ஜிபிஎஸ்",
                toolShelters = "பாதுகாப்பான புகலிடங்கள்",
                toolContacts = "அவசர தொடர்புகள்",
                toolAssessment = "ஆபத்து மதிப்பீடு",
                toolCompass = "வெளியேற்ற திசைகாட்டி",
                toolFirstAid = "முதலுதவி கையேடு",
                toolCalculator = "வாழ்வாதார கால்குலேட்டர்",
                toolChecklist = "அவசர சரிபார்ப்புப் பட்டியல்",
                selectLanguageTitle = "அவசர கால மொழியைத் தேர்ந்தெடுக்கவும்",
                selectLanguageSubtitle = "அனைத்து எச்சரிக்கைகள், குரல் வாசிப்பு, சென்சார் தகவல்கள் மற்றும் வழிகாட்டிகள் உடனடியாக உங்கள் மொழிக்கு மாற்றப்படும்.",
                voiceSupportedBadge = "குரல் வாசிப்பு வசதி உள்ளது",
                changeLanguageSuccess = "மொழி தமிழாக மாற்றப்பட்டது"
            )
        }
    }

    fun getLocalizedSeverityLabel(severity: AlertSeverity, language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> when (severity) {
                AlertSeverity.LOW -> "Low Intensity"
                AlertSeverity.MODERATE -> "Moderate Intensity"
                AlertSeverity.HIGH -> "High Intensity"
                AlertSeverity.SEVERE -> "Severe Intensity"
            }
            AppLanguage.HINDI -> when (severity) {
                AlertSeverity.LOW -> "कम तीव्रता"
                AlertSeverity.MODERATE -> "मध्यम तीव्रता"
                AlertSeverity.HIGH -> "उच्च तीव्रता"
                AlertSeverity.SEVERE -> "गंभीर तीव्रता"
            }
            AppLanguage.KANNADA -> when (severity) {
                AlertSeverity.LOW -> "ಕಡಿಮೆ ತೀವ್ರತೆ"
                AlertSeverity.MODERATE -> "ಮಧ್ಯಮ ತೀವ್ರತೆ"
                AlertSeverity.HIGH -> "ಹೆಚ್ಚಿನ ತೀವ್ರತೆ"
                AlertSeverity.SEVERE -> "ತೀವ್ರ ತೀವ್ರತೆ"
            }
            AppLanguage.TELUGU -> when (severity) {
                AlertSeverity.LOW -> "తక్కువ తీవ్రత"
                AlertSeverity.MODERATE -> "మోస్తరు తీవ్రత"
                AlertSeverity.HIGH -> "అధిక తీవ్రత"
                AlertSeverity.SEVERE -> "తీవ్రమైన తీవ్రత"
            }
            AppLanguage.TAMIL -> when (severity) {
                AlertSeverity.LOW -> "குறைந்த தீவிரம்"
                AlertSeverity.MODERATE -> "மிதமான தீவிரம்"
                AlertSeverity.HIGH -> "அதிக தீவிரம்"
                AlertSeverity.SEVERE -> "கடுமையான தீவிரம்"
            }
        }
    }

    fun getAlerts(language: AppLanguage): List<DisasterAlert> {
        return when (language) {
            AppLanguage.ENGLISH -> listOf(
                DisasterAlert(
                    id = "alert_hurricane_01",
                    title = "Hurricane Alert",
                    issuedBy = "Issued by Karnataka Government",
                    location = "MV Extension, Hoskote",
                    timestamp = "09:12 IST, 24 Aug 2023",
                    severity = AlertSeverity.LOW,
                    cardType = AlertCardType.WHITE_CARD,
                    description = "Atmospheric depression developing over southern plains. Wind gusts reaching 55-65 km/h with moderate rainfall warnings.",
                    dos = listOf(
                        "Anchor or secure all lightweight rooftop objects and loose garden furniture.",
                        "Keep flashlights, emergency power banks, and portable radio tuned to state alerts.",
                        "Store at least 15 liters of potable drinking water per person.",
                        "Stay indoors away from exposed glass windows and tall metal structures."
                    ),
                    donts = listOf(
                        "Do not step outdoors during high gust advisories unless directed to evacuate.",
                        "Do not touch broken or sagging electrical wires; report immediately to 112.",
                        "Do not park vehicles under aged or decayed trees.",
                        "Do not believe unverified social media forwards; rely strictly on official NDMA alerts."
                    )
                ),
                DisasterAlert(
                    id = "alert_flood_02",
                    title = "Flood Alert",
                    issuedBy = "Issued by Karnataka Government",
                    location = "MV Extension, Hoskote",
                    timestamp = "09:12 IST, 24 Aug 2023",
                    severity = AlertSeverity.SEVERE,
                    cardType = AlertCardType.PEACH_CARD,
                    description = "Severe water accumulation alert along river basin and low-lying canals following torrential rainfall upstream.",
                    dos = listOf(
                        "Immediately move elderly family members and pets to higher floors or relief camps.",
                        "Turn off the primary domestic power switch and shut off LPG cylinder valves.",
                        "Carry essential prescription medications, waterproof document pouch, and emergency ration.",
                        "Follow official designated evacuation corridors mapped in Disaster Guard."
                    ),
                    donts = listOf(
                        "Do not attempt to walk, swim, or drive through flowing floodwaters.",
                        "Do not drink untreated municipal tap water; boil vigorously before consumption.",
                        "Do not enter flooded basements or underground parking garages.",
                        "Do not spread panic; assist vulnerable neighbors with evacuation."
                    )
                ),
                DisasterAlert(
                    id = "alert_earthquake_03",
                    title = "Earthquake Alert",
                    issuedBy = "Issued by Karnataka Government",
                    location = "MV Extension, Hoskote",
                    timestamp = "09:12 IST, 24 Aug 2023",
                    severity = AlertSeverity.HIGH,
                    cardType = AlertCardType.CORAL_CARD,
                    description = "Deep seismic tremor detected along regional faultline. Aftershock advisories active for the next 24 hours.",
                    dos = listOf(
                        "DROP to your hands and knees immediately.",
                        "COVER your head and neck under a sturdy table or desk.",
                        "HOLD ON until the shaking stops.",
                        "Use staircases for exit after tremors cease; avoid all elevators."
                    ),
                    donts = listOf(
                        "Do not stand near exterior glass walls, hanging chandeliers, or heavy bookcases.",
                        "Do not light matches or use lighters due to post-quake gas line hazards.",
                        "Do not run out of high-rise structures during active shaking.",
                        "Do not tie up phone lines; use emergency SMS or Disaster Guard mesh."
                    )
                )
            )

            AppLanguage.HINDI -> listOf(
                DisasterAlert(
                    id = "alert_hurricane_01",
                    title = "चक्रवाती तूफान चेतावनी",
                    issuedBy = "कर्नाटक सरकार द्वारा जारी",
                    location = "एमवी एक्सटेंशन, होसकोटे",
                    timestamp = "09:12 IST, 24 अगस्त 2023",
                    severity = AlertSeverity.LOW,
                    cardType = AlertCardType.WHITE_CARD,
                    description = "दक्षिणी मैदानों पर गहरा अवदाब क्षेत्र बन रहा है। 55-65 किमी/घंटा की गति से तेज हवाओं और मध्यम वर्षा की चेतावनी।",
                    dos = listOf(
                        "छत की हल्की वस्तुओं और खुले फर्नीचर को कसकर बांधें या सुरक्षित करें।",
                        "टॉर्च, इमरजेंसी पावर बैंक और पोर्टेबल रेडियो को सक्रिय रखें।",
                        "प्रति व्यक्ति कम से कम 15 लीटर सुरक्षित पेयजल संग्रहित करें।",
                        "खिड़कियों और ऊंचे धातु संरचनाओं से दूर घर के अंदर सुरक्षित रहें।"
                    ),
                    donts = listOf(
                        "तेज हवाओं के दौरान जब तक निकासी का आदेश न हो, बाहर न निकलें।",
                        "टूटे या लटकते बिजली के तारों को न छुएं; तुरंत 112 पर रिपोर्ट करें।",
                        "पुराने या सूखे पेड़ों के नीचे वाहन पार्क न करें।",
                        "सोशल मीडिया की असत्यापित अफवाहों पर विश्वास न करें; केवल NDMA पर भरोसा करें।"
                    )
                ),
                DisasterAlert(
                    id = "alert_flood_02",
                    title = "बाढ़ की गंभीर चेतावनी",
                    issuedBy = "कर्नाटक सरकार द्वारा जारी",
                    location = "एमवी एक्सटेंशन, होसकोटे",
                    timestamp = "09:12 IST, 24 अगस्त 2023",
                    severity = AlertSeverity.SEVERE,
                    cardType = AlertCardType.PEACH_CARD,
                    description = "ऊपरी क्षेत्रों में अत्यधिक वर्षा के कारण नदी बेसिन और निचले नालों में तेजी से जलस्तर बढ़ने की गंभीर चेतावनी।",
                    dos = listOf(
                        "बुजुर्गों, बच्चों और पालतू जानवरों को तुरंत ऊपरी मंजिल या राहत शिविरों में ले जाएं।",
                        "घर का मुख्य बिजली स्विच बंद करें और रसोई गैस सिलेंडर के रेगुलेटर बंद करें।",
                        "आवश्यक दवाएं, वाटरप्रूफ बैग में जरूरी दस्तावेज और सूखा राशन साथ रखें।",
                        "जीवनसेतु मैप में चिन्हित आधिकारिक सुरक्षित निकासी गलियारों का पालन करें।"
                    ),
                    donts = listOf(
                        "बहते बाढ़ के पानी में पैदल चलने, तैरने या गाड़ी चलाने का प्रयास न करें।",
                        "नल का अशुद्ध पानी न पिएं; पीने से पहले पानी को अच्छी तरह उबालें।",
                        "जलमग्न बेसमेंट या भूमिगत पार्किंग में प्रवेश न करें।",
                        "घबराहट न फैलाएं; जरूरतमंद पड़ोसियों की सुरक्षित निकासी में सहायता करें।"
                    )
                ),
                DisasterAlert(
                    id = "alert_earthquake_03",
                    title = "भूकंप चेतावनी",
                    issuedBy = "कर्नाटक सरकार द्वारा जारी",
                    location = "एमवी एक्सटेंशन, होसकोटे",
                    timestamp = "09:12 IST, 24 अगस्त 2023",
                    severity = AlertSeverity.HIGH,
                    cardType = AlertCardType.CORAL_CARD,
                    description = "क्षेत्रीय भ्रंश रेखा पर गहरा भूकंपीय झटका दर्ज हुआ। अगले 24 घंटों तक आफ्टरशॉक की संभावना बनी हुई है।",
                    dos = listOf(
                        "तुरंत जमीन पर झुकें (DROP) और घुटनों के बल बैठ जाएं।",
                        "मजबूत मेज या डेस्क के नीचे सिर और गर्दन को ढकें (COVER)।",
                        "कंपन रुकने तक मजबूती से पकड़े रहें (HOLD ON)।",
                        "झटके रुकने के बाद बाहर निकलने के लिए केवल सीढ़ियों का उपयोग करें।"
                    ),
                    donts = listOf(
                        "कांच की खिड़कियों, भारी अलमारियों या झूमरों के पास खड़े न हों।",
                        "गैस रिसाव के खतरे के कारण माचिस या लाइटर न जलाएं।",
                        "कंपन के दौरान बहुमंजिला इमारतों से बाहर भागने की कोशिश न करें।",
                        "फोन लाइनों को व्यस्त न रखें; केवल आपातकालीन एसएमएस का प्रयोग करें।"
                    )
                )
            )

            AppLanguage.KANNADA -> listOf(
                DisasterAlert(
                    id = "alert_hurricane_01",
                    title = "ಚಂಡಮಾರುತದ ಎಚ್ಚರಿಕೆ",
                    issuedBy = "ಕರ್ನಾಟಕ ಸರ್ಕಾರದಿಂದ ಜಾರಿಗೊಳಿಸಲಾಗಿದೆ",
                    location = "ಎಂವಿ ಬಡಾವಣೆ, ಹೊಸಕೋಟೆ",
                    timestamp = "09:12 IST, 24 ಆಗಸ್ಟ್ 2023",
                    severity = AlertSeverity.LOW,
                    cardType = AlertCardType.WHITE_CARD,
                    description = "ದಕ್ಷಿಣ ಬಯಲು ಪ್ರದೇಶಗಳಲ್ಲಿ ವಾಯುಭಾರ ಕುಸಿತ ಉಂಟಾಗುತ್ತಿದೆ. 55-65 ಕಿಮೀ/ಗಂಟೆ ವೇಗದ ಬಿರುಗಾಳಿ ಮತ್ತು ಸಾಧಾರಣ ಮಳೆಯ ಮುನ್ಸೂಚನೆ ನೀಡಲಾಗಿದೆ.",
                    dos = listOf(
                        "ಮೇಲ್ಛಾವಣಿಯ ಹಗುರವಾದ ವಸ್ತುಗಳು ಮತ್ತು ಗಾರ್ಡನ್ ಪೀಠೋಪಕರಣಗಳನ್ನು ಬಿಗಿಯಾಗಿ ಕಟ್ಟಿ ಭದ್ರಪಡಿಸಿ.",
                        "ಟಾರ್ಚ್, ತುರ್ತು ಪವರ್ ಬ್ಯಾಂಕ್ ಮತ್ತು ರೇಡಿಯೋಗಳನ್ನು ಸನ್ನದ್ಧವಾಗಿಡಿ.",
                        "ಪ್ರತಿ ವ್ಯಕ್ತಿಗೆ ಕನಿಷ್ಠ 15 ಲೀಟರ್ ಕುಡಿಯುವ ನೀರನ್ನು ಸಂಗ್ರಹಿಸಿಡಿ.",
                        "ಕಿಟಕಿಗಳು ಮತ್ತು ಎತ್ತರದ ಲೋಹದ ಕಂಬಗಳಿಂದ ದೂರವಿರಿ, ಮನೆಯೊಳಗೆ ಸುರಕ್ಷಿತವಾಗಿರಿ."
                    ),
                    donts = listOf(
                        "ಸ್ಥಳಾಂತರಕ್ಕೆ ಸೂಚಿಸದ ಹೊರತು ಬಿರುಗಾಳಿ ಬೀಸುವ ಸಮಯದಲ್ಲಿ ಹೊರಗೆ ಹೋಗಬೇಡಿ.",
                        "ತುಂಡಾದ ಅಥವಾ ಜೋತುಬಿದ್ದ ವಿದ್ಯುತ್ ತಂತಿಗಳನ್ನು ಮುಟ್ಟಬೇಡಿ; ತಕ್ಷಣ 112 ಗೆ ಕರೆ ಮಾಡಿ.",
                        "ಹಳೆಯ ಅಥವಾ ಒಣಗಿದ ಮರಗಳ ಕೆಳಗೆ ವಾಹನಗಳನ್ನು ನಿಲ್ಲಿಸಬೇಡಿ.",
                        "ಸಾಮಾಜಿಕ ಜಾಲತಾಣದ ವದಂತಿಗಳನ್ನು ನಂಬಬೇಡಿ; ಕೇವಲ ಅಧಿಕೃತ NDMA ಎಚ್ಚರಿಕೆಗಳನ್ನು ನಂಬಿ."
                    )
                ),
                DisasterAlert(
                    id = "alert_flood_02",
                    title = "ಪ್ರವಾಹದ ತೀವ್ರ ಎಚ್ಚರಿಕೆ",
                    issuedBy = "ಕರ್ನಾಟಕ ಸರ್ಕಾರದಿಂದ ಜಾರಿಗೊಳಿಸಲಾಗಿದೆ",
                    location = "ಎಂವಿ ಬಡಾವಣೆ, ಹೊಸಕೋಟೆ",
                    timestamp = "09:12 IST, 24 ಆಗಸ್ಟ್ 2023",
                    severity = AlertSeverity.SEVERE,
                    cardType = AlertCardType.PEACH_CARD,
                    description = "ಮೇಲ್ಭಾಗದಲ್ಲಿ ನಿರಂತರ ಭಾರಿ ಮಳೆಯಿಂದಾಗಿ ನದಿ ಪಾತ್ರ ಮತ್ತು ತಗ್ಗು ಪ್ರದೇಶಗಳ ಕಾಲುವೆಗಳಲ್ಲಿ ತೀವ್ರ ಪ್ರವಾಹ ಪರಿಸ್ಥಿತಿ ಉಂಟಾಗಿದೆ.",
                    dos = listOf(
                        "ವೃದ್ಧರು, ಮಕ್ಕಳು ಮತ್ತು ಸಾಕುಪ್ರಾಣಿಗಳನ್ನು ತಕ್ಷಣವೇ ಮೇಲಿನ ಮಹಡಿಗೆ ಅಥವಾ ಪರಿಹಾರ ಕೇಂದ್ರಗಳಿಗೆ ಸ್ಥಳಾಂತರಿಸಿ.",
                        "ಮನೆಯ ಮುಖ್ಯ ವಿದ್ಯುತ್ ಸ್ವಿಚ್ ಮತ್ತು ಗ್ಯಾಸ್ ಸಿಲಿಂಡರ್ ಕವಾಟಗಳನ್ನು ಆಫ್ ಮಾಡಿ.",
                        "ಅಗತ್ಯ ಔಷಧಿಗಳು, ಜಲನಿರೋಧಕ ಚೀಲದಲ್ಲಿ ಮುಖ್ಯ ದಾಖಲೆಗಳು ಮತ್ತು ಒಣ ಆಹಾರವನ್ನು ಜೊತೆಗೆ ಇರಿಸಿ.",
                        "ಜೀವನಸೇತು ನಕ್ಷೆಯಲ್ಲಿ ಗುರುತಿಸಲಾದ ಅಧಿಕೃತ ಸುರಕ್ಷಿತ ಸ್ಥಳಾಂತರ ಮಾರ್ಗಗಳನ್ನು ಅನುಸರಿಸಿ."
                    ),
                    donts = listOf(
                        "ಹರಿಯುವ ಪ್ರವಾಹದ ನೀರಿನಲ್ಲಿ ನಡೆಯಲು, ಈಜಲು ಅಥವಾ ವಾಹನ ಚಲಾಯಿಸಲು ಪ್ರಯತ್ನಿಸಬೇಡಿ.",
                        "ಶುದ್ಧೀಕರಿಸದ ನೀರನ್ನು ಕುಡಿಯಬೇಡಿ; ಕುಡಿಯುವ ಮುನ್ನ ನೀರನ್ನು ಚೆನ್ನಾಗಿ ಕುದಿಸಿ.",
                        "ನೀರು ತುಂಬಿದ ನೆಲಮಾಳಿಗೆ ಅಥವಾ ಅಂಡರ್‌ಗ್ರೌಂಡ್ ಪಾರ್ಕಿಂಗ್‌ಗೆ ಪ್ರವೇಶಿಸಬೇಡಿ.",
                        "ಆತಂಕ ಹರಡಬೇಡಿ; ನೆರೆಹೊರೆಯವರಿಗೆ ಸ್ಥಳಾಂತರಗೊಳ್ಳಲು ಸಹಾಯ ಮಾಡಿ."
                    )
                ),
                DisasterAlert(
                    id = "alert_earthquake_03",
                    title = "ಭೂಕಂಪದ ಎಚ್ಚರಿಕೆ",
                    issuedBy = "ಕರ್ನಾಟಕ ಸರ್ಕಾರದಿಂದ ಜಾರಿಗೊಳಿಸಲಾಗಿದೆ",
                    location = "ಎಂವಿ ಬಡಾವಣೆ, ಹೊಸಕೋಟೆ",
                    timestamp = "09:12 IST, 24 ಆಗಸ್ಟ್ 2023",
                    severity = AlertSeverity.HIGH,
                    cardType = AlertCardType.CORAL_CARD,
                    description = "ಪ್ರಾದೇಶಿಕ ಭೂಕಂಪನ ರೇಖೆಯಲ್ಲಿ ಆಳವಾದ ಕಂಪನ ಸಂಭವಿಸಿದೆ. ಮುಂದಿನ 24 ಗಂಟೆಗಳ ಕಾಲ ನಂತರದ ಕಂಪನಗಳ ಸಾಧ್ಯತೆ ಇದೆ.",
                    dos = listOf(
                        "ತಕ್ಷಣವೇ ನೆಲಕ್ಕೆ ಮಂಡಿಯೂರಿ ಕುಳಿತುಕೊಳ್ಳಿ (DROP).",
                        "ಬಲವಾದ ಮೇಜು ಅಥವಾ ಡೆಸ್ಕ್ ಕೆಳಗೆ ತಲೆ ಮತ್ತು ಕುತ್ತಿಗೆಯನ್ನು ರಕ್ಷಿಸಿಕೊಳ್ಳಿ (COVER).",
                        "ಕಂಪನ ನಿಲ್ಲುವವರೆಗೂ ಬಲವಾಗಿ ಹಿಡಿದುಕೊಳ್ಳಿ (HOLD ON).",
                        "ಕಂಪನ ನಿಂತ ನಂತರ ಹೊರಬರಲು ಲಿಫ್ಟ್ ಬಳಸಬೇಡಿ, ಮೆಟ್ಟಿಲುಗಳನ್ನು ಮಾತ್ರ ಬಳಸಿ."
                    ),
                    donts = listOf(
                        "ಗಾಜಿನ ಕಿಟಕಿಗಳು, ಭಾರವಾದ ಕಪಾಟುಗಳು ಅಥವಾ ನೇತಾಡುವ ದೀಪಗಳ ಬಳಿ ನಿಲ್ಲಬೇಡಿ.",
                        "ಅನಿಲ ಸೋರಿಕೆಯ ಅಪಾಯವಿರುವುದರಿಂದ ಬೆಂಕಿಪೊಟ್ಟಣ ಅಥವಾ ಲೈಟರ್ ಹಚ್ಚಬೇಡಿ.",
                        "ಕಂಪನದ ಸಮಯದಲ್ಲಿ ಕಟ್ಟಡದಿಂದ ಹೊರಗೆ ಓಡಲು ಯತ್ನಿಸಬೇಡಿ.",
                        "ಫೋನ್ ಕರೆಗಳನ್ನು ಅತಿಯಾಗಿ ಮಾಡಬೇಡಿ; ಕೇವಲ ತುರ್ತು SMS ಬಳಸಿ."
                    )
                )
            )

            AppLanguage.TELUGU -> listOf(
                DisasterAlert(
                    id = "alert_hurricane_01",
                    title = "తుఫాను హెచ్చరిక",
                    issuedBy = "కర్ణాటక ప్రభుత్వం జారీ చేసింది",
                    location = "ఎంవీ ఎక్స్‌టెన్షన్, హోస్కోటే",
                    timestamp = "09:12 IST, 24 ఆగస్టు 2023",
                    severity = AlertSeverity.LOW,
                    cardType = AlertCardType.WHITE_CARD,
                    description = "దక్షిణ మైదాన ప్రాంతాల్లో వాయుగుండం ఏర్పడుతోంది. గంటకు 55-65 కి.మీ వేగంతో బలమైన ఈదురుగాలులు, మోస్తరు వర్ష సూచన.",
                    dos = listOf(
                        "పైకప్పుపై ఉన్న తేలికపాటి వస్తువులు, అవుట్‌డోర్ ఫర్నిచర్‌ను సురక్షితంగా కట్టి ఉంచండి.",
                        "టార్చ్ లైట్లు, ఎమర్జెన్సీ పవర్ బ్యాంకులు, రేడియోలను సిద్ధంగా ఉంచండి.",
                        "ఒక్కొక్కరికి కనీసం 15 లీటర్ల త్రాగునీటిని నిల్వ చేసుకోండి.",
                        "కిటికీలకు, ఎత్తైన లోహ స్తంభాలకు దూరంగా ఇంటి లోపలే సురక్షితంగా ఉండండి."
                    ),
                    donts = listOf(
                        "తీవ్రమైన గాలులు వీచేటప్పుడు అత్యవసరమైతే తప్ప బయటకు రావద్దు.",
                        "తెగిపడిన లేదా వేలాడుతున్న విద్యుత్ తీగలను తాకవద్దు; వెంటనే 112 కు సమాచారం ఇవ్వండి.",
                        "పాతబడిన లేదా ఎండిపోయిన చెట్ల కింద వాహనాలను పార్క్ చేయవద్దు.",
                        "సోషల్ మీడియా పుకార్లను నమ్మవద్దు; అధికారిక NDMA మార్గదర్శకాలను మాత్రమే పాటించండి."
                    )
                ),
                DisasterAlert(
                    id = "alert_flood_02",
                    title = "తీవ్ర వరద హెచ్చరిక",
                    issuedBy = "కర్ణాటక ప్రభుత్వం జారీ చేసింది",
                    location = "ఎంవీ ఎక్స్‌టెన్షన్, హోస్కోటే",
                    timestamp = "09:12 IST, 24 ఆగస్టు 2023",
                    severity = AlertSeverity.SEVERE,
                    cardType = AlertCardType.PEACH_CARD,
                    description = "ఎగువన కురిసిన కుండపోత వర్షాల వల్ల నదీ పరివాహక ప్రాంతాలు మరియు లోతట్టు కాలువలలో తీవ్రమైన వరద ముప్పు పొంచి ఉంది.",
                    dos = listOf(
                        "వృద్ధులు, పిల్లలు, పెంపుడు జంతువులను వెంటనే పై అంతస్తులకు లేదా పునరావాస కేంద్రాలకు తరలించండి.",
                        "ఇంటి ప్రధాన విద్యుత్ స్విచ్ మరియు గ్యాస్ సిలిండర్ రెగ్యులేటర్‌ను ఆపివేయండి.",
                        "ముఖ్యమైన మందులు, వాటర్‌ప్రూఫ్ కవర్లలో పత్రాలు, అత్యవసర ఆహారాన్ని వెంట ఉంచుకోండి.",
                        "జీవనసేతు మ్యాప్‌లో సూచించిన అధికారిక తరలింపు మార్గాలను అనుసరించండి."
                    ),
                    donts = listOf(
                        "ప్రవహిస్తున్న వరద నీటిలో నడవడానికి, ఈదడానికి లేదా డ్రైవ్ చేయడానికి ప్రయత్నించవద్దు.",
                        "శుద్ధి చేయని మునిసిపల్ నీటిని తాగవద్దు; తాగేముందు నీటిని బాగా మరిగించండి.",
                        "నీరు చేరిన నేలమాళిగలు లేదా అండర్‌గ్రౌండ్ పార్కింగ్‌లలోకి వెళ్లవద్దు.",
                        "ఆందోళన వ్యాప్తి చేయవద్దు; తోటి వారికి సురక్షిత తరలింపులో సహాయపడండి."
                    )
                ),
                DisasterAlert(
                    id = "alert_earthquake_03",
                    title = "భూకంప హెచ్చరిక",
                    issuedBy = "కర్ణాటక ప్రభుత్వం జారీ చేసింది",
                    location = "ఎంవీ ఎక్స్‌టెన్షన్, హోస్కోటే",
                    timestamp = "09:12 IST, 24 ఆగస్టు 2023",
                    severity = AlertSeverity.HIGH,
                    cardType = AlertCardType.CORAL_CARD,
                    description = "ప్రాంతీయ ఫాల్ట్‌లైన్ వెంబడి తీవ్ర భూకంప ప్రకంపనలు నమోదయ్యాయి. రాబోయే 24 గంటలపాటు తదుపరి ప్రకంపనలు సంభవించే అవకాశం ఉంది.",
                    dos = listOf(
                        "వెంటనే నేలపై మోకరిల్లండి (DROP).",
                        "బలమైన టేబుల్ లేదా డెస్క్ కింద తల మరియు మెడను రక్షించుకోండి (COVER).",
                        "కంపనాలు ఆగే వరకు గట్టిగా పట్టుకుని ఉండండి (HOLD ON).",
                        "కంపనాలు ఆగిన తర్వాత బయటకు రావడానికి మెట్లను మాత్రమే ఉపయోగించండి, లిఫ్టులను వాడవద్దు."
                    ),
                    donts = listOf(
                        "గాజు కిటికీలు, భారీ పుస్తకాల అరలు లేదా వేలాడే దీపాల దగ్గర నిలబడవద్దు.",
                        "గ్యాస్ లీక్ ప్రమాదం ఉన్నందున అగ్గిపుల్లలు లేదా లైటర్లను వెలిగించవద్దు.",
                        "కంపనాలు జరుగుతున్న సమయంలో బహుళ అంతస్తుల భవనాల నుండి బయటకు పరిగెత్తవద్దు.",
                        "ఫోన్ లైన్లను నిరంతరం వాడవద్దు; కేవలం అత్యవసర SMS మాత్రమే ఉపయోగించండి."
                    )
                )
            )

            AppLanguage.TAMIL -> listOf(
                DisasterAlert(
                    id = "alert_hurricane_01",
                    title = "புயல் எச்சரிக்கை",
                    issuedBy = "கர்நாடக அரசால் வெளியிடப்பட்டது",
                    location = "எம்.வி விரிவாக்கம், ஹோஸ்கோட்",
                    timestamp = "09:12 IST, 24 ஆகஸ்ட் 2023",
                    severity = AlertSeverity.LOW,
                    cardType = AlertCardType.WHITE_CARD,
                    description = "தென் சமவெளிப் பகுதியில் காற்றழுத்த தாழ்வு மண்டலம் தீவிரமடைகிறது. மணிக்கு 55-65 கிமீ வேகத்தில் பலத்த காற்று மற்றும் மிதமான மழை எச்சரிக்கை.",
                    dos = listOf(
                        "கூரை ಮೇಲಿನ இலகுவான பொருட்கள் மற்றும் வெளிப்புற நாற்காலிகளைப் பாதுகாப்பாகக் கட்டுங்கள்.",
                        "டார்ச் விளக்குகள், பவர் பாங்குகள் மற்றும் வானொலியைத் தயாராக வையுங்கள்.",
                        "ஒரு நபருக்குக் குறைந்தபட்சம் 15 லிட்டர் குடிநீரைச் சேமித்து வையுங்கள்.",
                        "கண்ணாடி ஜன்னல்கள் மற்றும் உயரமான உலோகக் கம்பங்களை விட்டு விலகி வீட்டிற்குள் பாதுகாப்பாக இருங்கள்."
                    ),
                    donts = listOf(
                        "வெளியேற உத்தரவிடாத வரை பலத்த காற்றின் போது வீட்டை விட்டு வெளியேறாதீர்கள்.",
                        "அறுந்து கிடக்கும் மின்கம்பிகளைத் தொடாதீர்கள்; உடனடியாக 112-க்குத் தெரிவிக்கவும்.",
                        "பழைய அல்லது காய்ந்த மரங்களின் கீழ் வாகனங்களை நிறுத்தாதீர்கள்.",
                        "வதந்திகளை நம்பாதீர்கள்; அதிகாரப்பூர்வ NDMA எச்சரிக்கைகளை மட்டுமே பின்பற்றுங்கள்."
                    )
                ),
                DisasterAlert(
                    id = "alert_flood_02",
                    title = "கடும் வெள்ள எச்சரிக்கை",
                    issuedBy = "கர்நாடக அரசால் வெளியிடப்பட்டது",
                    location = "எம்.வி விரிவாக்கம், ஹோஸ்கோட்",
                    timestamp = "09:12 IST, 24 ஆகஸ்ட் 2023",
                    severity = AlertSeverity.SEVERE,
                    cardType = AlertCardType.PEACH_CARD,
                    description = "மேற்பகுதியில் பெய்த கனமழையால் நதிப் படுகை மற்றும் தாழ்வான கால்வாய்களில் கடுமையான வெள்ளப் பெருக்கு ஏற்பட்டுள்ளது.",
                    dos = listOf(
                        "முதியவர்கள், குழந்தைகள் மற்றும் செல்லப்பிராணிகளை உடனடியாக மேல் தளத்திற்கு அல்லது நிவாரண முகாமிற்கு மாற்றவும்.",
                        "முதன்மை மின்சார சுவிட்ச் மற்றும் கேஸ் சிலிண்டர் வால்வை அணைக்கவும்.",
                        "அத்தியாவசிய மருந்துகள், நீர்ப்புகா பையில் முக்கிய ஆவணங்கள் மற்றும் உலர் உணவை உடன் வைத்திருக்கவும்.",
                        "ஜீவன்சேது வரைபடத்தில் குறிப்பிடப்பட்டுள்ள பாதுகாப்பான வெளியேற்றப் பாதைகளைப் பின்பற்றவும்."
                    ),
                    donts = listOf(
                        "ஓடும் வெள்ள நீரில் நடக்கவோ, நீந்தவோ அல்லது வாகனம் ஓட்டவோ முயற்சிக்காதீர்கள்.",
                        "சுத்திகரிக்கப்படாத குடிநீரைக் குடிக்காதீர்கள்; குடிக்கும் முன் நீரைக் கொதிக்க வைக்கவும்.",
                        "வெள்ளம் சூழ்ந்த அடித்தளங்கள் அல்லது நிலத்தடி பார்க்கிங் பகுதிக்குள் நுழையாதீர்கள்.",
                        "பயத்தை பரப்பாதீர்கள்; பக்கத்து வீட்டினருக்குப் பாதுகாப்பாக வெளியேற உதவுங்கள்."
                    )
                ),
                DisasterAlert(
                    id = "alert_earthquake_03",
                    title = "நிலநடுக்க எச்சரிக்கை",
                    issuedBy = "கர்நாடக அரசால் வெளியிடப்பட்டது",
                    location = "எம்.வி விரிவாக்கம், ஹோஸ்கோட்",
                    timestamp = "09:12 IST, 24 ஆகஸ்ட் 2023",
                    severity = AlertSeverity.HIGH,
                    cardType = AlertCardType.CORAL_CARD,
                    description = "பிராந்திய நில அதிர்வு மண்டலத்தில் கடுமையான நடுக்கம் உணரப்பட்டது. அடுத்த 24 மணி நேரத்திற்குத் தொடர் நடுக்கங்கள் ஏற்பட வாய்ப்புள்ளது.",
                    dos = listOf(
                        "உடனடியாகத் தரையில் மண்டியிட்டு அமரவும் (DROP).",
                        "உறுதியான மேசையின் கீழ் தலை மற்றும் கழுத்தைப் பாதுகாத்துக் கொள்ளவும் (COVER).",
                        "நடுக்கம் நிற்கும் வரை மேசையைப் பிடித்துக் கொள்ளவும் (HOLD ON).",
                        "நடுக்கம் நின்ற பின் வெளியேறப் படிக்கட்டுகளை மட்டுமே பயன்படுத்தவும், மின்தூக்கியைப் பயன்படுத்தாதீர்கள்."
                    ),
                    donts = listOf(
                        "கண்ணாடி ஜன்னல்கள், கனமான புத்தக அலமாரிகள் அல்லது தொங்கும் விளக்குகள் அருகே நிற்காதீர்கள்.",
                        "எரிவாயுக் கசிவு அபாயம் உள்ளதால் தீக்குச்சிகள் அல்லது லைட்டர்களைப் பற்றவைக்காதீர்கள்.",
                        "நடுக்கத்தின் போது உயரமான கட்டடங்களிலிருந்து வெளியே ஓட முயற்சிக்காதீர்கள்.",
                        "தொலைபேசி இணைப்புகளைத் தேவையின்றிப் பயன்படுத்தாதீர்கள்; அவசர SMS மட்டுமே பயன்படுத்தவும்."
                    )
                )
            )
        }
    }
}
