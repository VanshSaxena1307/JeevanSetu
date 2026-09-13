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
        }
    }
}
