package com.example.data.repository

import com.example.domain.model.DisasterGuide
import com.example.domain.model.DisasterType
import com.example.domain.model.FirstAidTopic

class DisasterGuideRepository {

    fun getAllGuides(): List<DisasterGuide> = listOf(
        DisasterGuide(
            id = "flood",
            type = DisasterType.FLOOD,
            title = "Flood Emergency Survival Guide",
            summary = "Essential protocols for seasonal inundation, river surges, and urban flooding.",
            beforeSteps = listOf(
                "Identify higher ground elevation locations and safe community shelters in advance.",
                "Store at least 3-5 days of clean drinking water in tightly sealed containers.",
                "Download regional offline maps and bookmark elevated shelters in JeevanSetu.",
                "Pack all government IDs, deed papers, and insurance files in waterproof pouches.",
                "Move vulnerable electronics, documents, and emergency medications to top floors."
            ),
            duringSteps = listOf(
                "Avoid entering moving floodwater. Just 6 inches of rapid water can sweep an adult off their feet.",
                "Never drive through flooded roads or underpasses ('Turn Around, Don't Drown').",
                "If water enters your shelter, switch off the main electrical breaker immediately to prevent electrocution.",
                "Move vertically to the highest stable floor or roof. Take water, flashlight, and phone.",
                "Do NOT enter closed attics where you could become trapped if water continues to rise; ensure roof access."
            ),
            afterSteps = listOf(
                "Wait for official announcements confirming safety before returning to inundated zones.",
                "Avoid damaged structures, buckling walls, and downed powerlines in standing water.",
                "Thoroughly disinfect all surfaces touched by floodwater; assume floodwater is contaminated with sewage.",
                "Boil all water vigorously for at least 3 minutes before consuming, or use water purification tablets."
            ),
            doList = listOf(
                "Disconnect electrical appliances before water reaches floor sockets.",
                "Keep flashlights and battery radios accessible.",
                "Signal from high vantage points with bright cloths or whistles."
            ),
            dontList = listOf(
                "NEVER walk or swim through fast-flowing flood currents.",
                "NEVER touch submerged electrical switches or wire lines.",
                "NEVER drink floodwater without certified boiling and purification."
            ),
            highRiskThreshold = "Water depth exceeding 12 inches or rising faster than 2 inches per hour."
        ),
        DisasterGuide(
            id = "flash_flood",
            type = DisasterType.FLASH_FLOOD,
            title = "Flash Flood Rapid Action Guide",
            summary = "Immediate survival tactics for sudden, high-velocity canyon and urban water rushes.",
            beforeSteps = listOf(
                "Monitor heavy thunderstorm warnings in upstream river or hill catchment zones.",
                "Identify escape paths perpendicular to river ravines and storm drainage basins."
            ),
            duringSteps = listOf(
                "Move to higher ground immediately. You may have only seconds or minutes to react.",
                "Abandon vehicles if caught in rapidly rising water; climb atop the vehicle or seek higher elevation.",
                "Do not attempt to cross bridges over torrential debris torrents."
            ),
            afterSteps = listOf(
                "Stay on high ground until flash torrents recede completely.",
                "Watch for washed-out roadbeds, undermined pavement, and destabilized culverts."
            ),
            doList = listOf(
                "Head uphill immediately at the first rumble or sudden water rise.",
                "Help children and elderly to higher elevation points first."
            ),
            dontList = listOf(
                "NEVER underestimate water velocity; 1 foot of water carries thousands of pounds of force.",
                "NEVER park or camp alongside low-lying riverbeds during storm watches."
            ),
            highRiskThreshold = "Sudden torrent emergence or rapid drainage blockage."
        ),
        DisasterGuide(
            id = "earthquake",
            type = DisasterType.EARTHQUAKE,
            title = "Earthquake Seismic Survival Guide",
            summary = "Critical maneuvers during tremors, aftershock mitigation, and structural checks.",
            beforeSteps = listOf(
                "Secure heavy furniture, water heaters, and hanging light fixtures to wall studs.",
                "Identify safe cover spots in each room: under sturdy desks, interior tables, or against inner walls.",
                "Keep emergency shoes, whistles, and flashlights beside every bed."
            ),
            duringSteps = listOf(
                "DROP, COVER, and HOLD ON. Drop to your hands and knees. Cover your head and neck under sturdy furniture.",
                "Stay away from glass windows, exterior walls, chimneys, and loose overhead fixtures.",
                "If in bed, stay there and protect your head with a thick pillow.",
                "Do NOT run outdoors during shaking; falling glass and parapets cause the majority of exterior injuries.",
                "NEVER use elevators; stairwells must be inspected for cracks before use."
            ),
            afterSteps = listOf(
                "Check yourself and others for injuries. Apply direct pressure to bleeding wounds.",
                "Smell for natural gas leaks. If gas odor is detected, turn off main valve and evacuate immediately outdoors.",
                "Do NOT light matches, candles, or toggle electrical switches if gas leak is possible.",
                "Be prepared for severe aftershocks; each aftershock can collapse already weakened walls."
            ),
            doList = listOf(
                "Protect your head and vital organs with furniture or your arms.",
                "Wear thick sturdy shoes to protect against shattered glass.",
                "Check utility lines for rupture before using tap water."
            ),
            dontList = listOf(
                "NEVER stand in doorways; modern doorframes are not stronger than the rest of the wall.",
                "NEVER rush down staircases during violent active shaking."
            ),
            highRiskThreshold = "Magnitude > 6.0 or visible shearing fissures across load-bearing pillars."
        ),
        DisasterGuide(
            id = "landslide",
            type = DisasterType.LANDSLIDE,
            title = "Landslide & Mudflow Safety Guide",
            summary = "Early warning indicators, escape vectors, and slope destabilization rules.",
            beforeSteps = listOf(
                "Learn if your area has historically experienced debris flows or rock falls.",
                "Observe slope changes: new cracks in plaster, tilted trees, or suddenly muddy creek water."
            ),
            duringSteps = listOf(
                "If near an active slide, move away from the path of debris as fast as possible.",
                "Run perpendicular to the flow direction (sideways across the slope), never downhill along the path.",
                "If escape is impossible, curl into a tight fetal ball and protect your head with arms and hands."
            ),
            afterSteps = listOf(
                "Stay away from the slide area. Secondary slides often follow the initial destabilization.",
                "Check for injured or trapped people without directly entering the hazard slide footprint."
            ),
            doList = listOf(
                "Listen for unusual sounds such as cracking trees or roaring boulders.",
                "Evacuate valleys and ravines during intense prolonged downpours."
            ),
            dontList = listOf(
                "NEVER attempt to dig out heavy boulders alone in an active rain zone.",
                "NEVER stay in lower levels of buildings situated directly at the base of steep slopes."
            ),
            highRiskThreshold = "Active ground cracking or rapid hillside mud creeping."
        ),
        DisasterGuide(
            id = "cyclone",
            type = DisasterType.CYCLONE,
            title = "Cyclone, Hurricane & Severe Storm Guide",
            summary = "Hardening shelter, weathering high-category windstorms, and storm surge protocol.",
            beforeSteps = listOf(
                "Secure or bring inside all loose patio furniture, trash bins, and corrugated sheets.",
                "Tape or shutter windows; board large glass exposures with plywood if available.",
                "Stock 5 days of non-perishable rations, batteries, and potable drinking water.",
                "Identify whether your location falls within the coastal storm surge inundation sector."
            ),
            duringSteps = listOf(
                "Stay indoors in a small, windowless interior room, closet, or hallway on the lowest floor.",
                "Keep away from exterior glass doors and skylights.",
                "Beware the eye of the storm: a sudden lull in wind does NOT mean the storm is over; violent winds will resume from the opposite direction."
            ),
            afterSteps = listOf(
                "Inspect roof and electrical lines with extreme care.",
                "Do not step into standing puddle water near downed power poles."
            ),
            doList = listOf(
                "Stay tuned to battery-operated radio reports for eye passage updates.",
                "Keep a mattress or heavy blankets ready to shield against debris."
            ),
            dontList = listOf(
                "NEVER venture outside during temporary wind lulls without official clearance.",
                "NEVER operate fuel generators indoors (carbon monoxide asphyxiation hazard)."
            ),
            highRiskThreshold = "Sustained winds exceeding 90 km/h or storm surge warnings."
        ),
        DisasterGuide(
            id = "fire",
            type = DisasterType.FIRE,
            title = "Fire & Wildfire Evacuation Guide",
            summary = "Rapid egress, smoke inhalation defense, and containment defense.",
            beforeSteps = listOf(
                "Map at least two unobstructed escape paths from every room.",
                "Keep fire extinguishers serviced and familiarize family with the PASS technique.",
                "Clear dry brush, leaf debris, and flammable timber within 30 feet of exterior walls."
            ),
            duringSteps = listOf(
                "If fire erupts, evacuate immediately. Do NOT pause to collect personal belongings.",
                "Stay low beneath smoke: toxic hot gases rise; the cleanest, coolest air is within 12 inches of the floor.",
                "Feel doors with the back of your hand before turning knobs. If the door or knob is warm, do NOT open it.",
                "If clothes catch fire: STOP, DROP, and ROLL until flames are smothered."
            ),
            afterSteps = listOf(
                "Do NOT re-enter a burned building until fire marshals declare it structurally sound.",
                "Seek medical triage for even minor smoke inhalation (delayed lung edema risk)."
            ),
            doList = listOf(
                "Crawl on hands and knees under smoke toward exits.",
                "Close doors behind you to compartmentalize and slow flame advancement."
            ),
            dontList = listOf(
                "NEVER use elevators in a fire (power cuts will trap passengers).",
                "NEVER re-enter a burning structure for pets or valuables."
            ),
            highRiskThreshold = "Smoke entering shelter or flames visible within 50 meters."
        ),
        DisasterGuide(
            id = "heatwave",
            type = DisasterType.HEATWAVE,
            title = "Extreme Heatwave & Dehydration Guide",
            summary = "Mitigating thermal stress, heat exhaustion vs heat stroke, and cooling tactics.",
            beforeSteps = listOf(
                "Hang damp white curtains or reflective sheets over sun-facing windows.",
                "Stock Oral Rehydration Salts (ORS), coconut water, or electrolyte powder."
            ),
            duringSteps = listOf(
                "Drink water regularly even before feeling thirsty. Avoid caffeinated and alcoholic beverages.",
                "Keep in the coolest ground floor rooms; use damp cloths on wrists, neck, and forehead.",
                "Wear loose-fitting, light-colored cotton clothing.",
                "Watch for heat exhaustion symptoms: heavy sweating, paleness, muscle cramps, dizziness."
            ),
            afterSteps = listOf(
                "Monitor elderly neighbors and infants who are most susceptible to fatal heat stroke."
            ),
            doList = listOf(
                "Sip water in small, consistent quantities throughout peak heat hours (11am - 4pm).",
                "Take cool sponge baths to drop internal core temperature."
            ),
            dontList = listOf(
                "NEVER leave children or vulnerable people in parked vehicles even for a few minutes.",
                "NEVER engage in strenuous outdoor labor during peak daytime temperature hours."
            ),
            highRiskThreshold = "Ambient temperature > 42°C (107°F) with high humidity."
        ),
        DisasterGuide(
            id = "building_collapse",
            type = DisasterType.BUILDING_COLLAPSE,
            title = "Building Structural Collapse Guide",
            summary = "Survival void identification, signaling trapped searchers, and self-rescue.",
            beforeSteps = listOf(
                "Identify reinforced structural corners, concrete lintels, and triangular void zones."
            ),
            duringSteps = listOf(
                "Get next to a large, sturdy object (heavy sofa, reinforced desk) that creates a 'triangle of life' void.",
                "Protect your head and chest from crushing masonry debris.",
                "If trapped in rubble, cover your mouth with a cloth or shirt to filter concrete dust.",
                "Tap on pipes, metal beams, or walls rhythmically in sets of three so rescuers can hear you.",
                "Shout only as a last resort to conserve oxygen and avoid inhaling dangerous airborne dust."
            ),
            afterSteps = listOf(
                "Exit carefully, avoiding shifting debris that could trigger secondary pancake collapses."
            ),
            doList = listOf(
                "Tap rhythmically on pipes or hard objects to signal rescue dogs and acoustic sensors.",
                "Conserve your voice and stay calm to reduce oxygen consumption."
            ),
            dontList = listOf(
                "NEVER light matches or cigarette lighters (gas line explosion risk).",
                "NEVER kick violently at rubble piles that may be supporting overhead slabs."
            ),
            highRiskThreshold = "Audible concrete cracking or visible wall bowing."
        )
    )

    fun getAllFirstAidTopics(): List<FirstAidTopic> = listOf(
        FirstAidTopic(
            id = "bleeding",
            title = "Severe Bleeding Control",
            category = "Trauma",
            urgencyLevel = "URGENT",
            summary = "Immediate techniques to stop arterial and severe venous hemorrhage.",
            immediateActions = listOf(
                "Apply FIRM, DIRECT PRESSURE directly over the wound with sterile gauze, clean cloth, or bare hands if necessary.",
                "Do NOT remove the pressure cloth if blood seeps through; add more cloths directly on top and press harder.",
                "Elevate the injured limb above heart level if no fracture is suspected.",
                "Apply a snug pressure bandage to maintain continuous compressive force.",
                "For life-threatening limb arterial spurting uncontrollable by direct pressure, apply a commercial or improvised tourniquet 2-3 inches above wound (never over a joint). Note the exact application time."
            ),
            criticalDonts = listOf(
                "NEVER remove impaled objects (knives, glass, rebar) from a wound; stabilize them in place with rolled dressings.",
                "NEVER loosen a tourniquet once applied unless directed by qualified medical surgeons.",
                "NEVER peek under the dressing to see if bleeding stopped; doing so pulls away the newly formed blood clot."
            ),
            whenToSeekEvacuation = "Spurting bright red blood, numbness/paleness in limb, or victim showing signs of shock (confusion, cold clammy skin)."
        ),
        FirstAidTopic(
            id = "burns",
            title = "Burn Care (Thermal & Chemical)",
            category = "Skin / Thermal",
            urgencyLevel = "HIGH",
            summary = "First-degree to third-degree burn management and cooling protocol.",
            immediateActions = listOf(
                "COOL the burn immediately with clean, cool running water for at least 10-20 minutes.",
                "Remove constricting jewelry, belts, and tight clothing before the affected area begins to swell.",
                "COVER loosely with a sterile dressing, clean cotton cloth, or plastic food wrap.",
                "Keep the patient warm with clean blankets over unburned areas to prevent hypothermia.",
                "Elevate the burned limb to reduce edema and throbbing pain."
            ),
            criticalDonts = listOf(
                "NEVER apply ice, ice water, butter, grease, toothpaste, or oil on burns.",
                "NEVER pop or puncture intact blisters (intact skin prevents infection).",
                "NEVER pull away clothing that is firmly stuck/melted to charred burn tissue."
            ),
            whenToSeekEvacuation = "Burns covering large areas (> palm size), burns to face, hands, feet, groin, or any charred, leathery white skin."
        ),
        FirstAidTopic(
            id = "fractures",
            title = "Fractures & Bone Splinting",
            category = "Orthopedic",
            urgencyLevel = "HIGH",
            summary = "Stabilization of broken limbs and sprains during emergency evacuation.",
            immediateActions = listOf(
                "IMMOBILIZE the injured limb in the exact position you found it.",
                "Support the joint above and the joint below the suspected fracture using rigid boards, rolled newspapers, or sticks.",
                "Pad the splint with soft towels or fabric to prevent pressure sores.",
                "Secure the splint with bandages, cloth strips, or neckties without restricting blood circulation.",
                "Check for circulation (pulse, warmth, skin color in toes/fingers) every 15 minutes."
            ),
            criticalDonts = listOf(
                "NEVER attempt to push a protruding bone end back into the wound (open fracture).",
                "NEVER attempt to straighten or realign an angulated broken bone.",
                "NEVER let the casualty walk on a suspected fractured leg or ankle."
            ),
            whenToSeekEvacuation = "Bone piercing through skin, absence of pulse below the injury, or severe numbness/blue coloration."
        ),
        FirstAidTopic(
            id = "heat_exhaustion",
            title = "Heat Exhaustion & Heat Stroke",
            category = "Environmental",
            urgencyLevel = "URGENT",
            summary = "Differentiating and treating thermal overload and fatal heat stroke.",
            immediateActions = listOf(
                "Move the victim immediately to a cool, shaded, or ventilated area.",
                "Loosen or remove restrictive outer clothing.",
                "Cool the skin: apply cool wet cloths, sponge with water, and fan vigorously.",
                "Place ice packs or cool cloths on the neck, armpits, and groin where major blood vessels are superficial.",
                "If conscious and not vomiting, give small sips of cool water mixed with Oral Rehydration Salts (ORS)."
            ),
            criticalDonts = listOf(
                "NEVER give fluids if the victim is unconscious, drowsy, or having seizures (aspiration risk).",
                "NEVER administer aspirin or paracetamol for heat stroke (it will not reduce environmental hyperthermia and can damage liver/kidneys)."
            ),
            whenToSeekEvacuation = "Heat stroke emergency: confusion, altered mental state, slurred speech, seizures, or hot dry skin with cessation of sweating."
        ),
        FirstAidTopic(
            id = "hypothermia",
            title = "Hypothermia & Cold Shock",
            category = "Environmental",
            urgencyLevel = "HIGH",
            summary = "Rewarming protocols for individuals exposed to cold rain, floodwater, or blizzards.",
            immediateActions = listOf(
                "Gently move the person into a dry, wind-sheltered area.",
                "Remove wet clothing immediately and wrap the person in layers of dry blankets, sleeping bags, or thermal space foil.",
                "Cover the head, neck, and chest, leaving only the face exposed.",
                "Provide warm sweet beverages (tea, broth, honey water) ONLY if fully conscious and able to swallow.",
                "Share body warmth by lying next to the person under insulating blankets."
            ),
            criticalDonts = listOf(
                "NEVER rub or massage cold extremities (can trigger cardiac arrhythmias).",
                "NEVER use direct intense heat like hot water bottles directly on bare skin, radiant heaters, or open flames.",
                "NEVER give alcohol or tobacco."
            ),
            whenToSeekEvacuation = "Victim stops shivering, becomes incoherent, exhibits severe apathy, or loses consciousness."
        ),
        FirstAidTopic(
            id = "minor_wounds",
            title = "Lacerations, Punctures & Debris Cuts",
            category = "Wound Care",
            urgencyLevel = "MODERATE",
            summary = "Field disinfection to prevent sepsis and tetanus in disaster environments.",
            immediateActions = listOf(
                "Wash hands thoroughly with soap or hand sanitizer before dressing the wound.",
                "Rinse the cut with copious amounts of clean, drinkable water to flush out dirt and grit.",
                "Apply antiseptic solution (povidone iodine or chlorhexidine) or antibiotic cream.",
                "Cover with a sterile adhesive bandage or gauze pad.",
                "Inspect the wound daily for signs of spreading redness, heat, swelling, or yellow pus."
            ),
            criticalDonts = listOf(
                "NEVER wash wounds with untreated floodwater or river water.",
                "NEVER apply herbal pastes, dirt, or unsterilized household substances into open lacerations.",
                "NEVER close deep puncture wounds tightly with waterproof tape without thorough irrigation."
            ),
            whenToSeekEvacuation = "Deep gaping wound needing stitches, rusty nail puncture (tetanus risk), or red streaks radiating up the arm/leg."
        ),
        FirstAidTopic(
            id = "smoke_inhalation",
            title = "Smoke & Toxic Gas Inhalation",
            category = "Respiratory",
            urgencyLevel = "URGENT",
            summary = "Managing airway compromise and carbon monoxide/particulate distress.",
            immediateActions = listOf(
                "Remove casualty to fresh air immediately away from smoke plumes.",
                "Place in an upright seated or semi-reclined position to assist lung expansion.",
                "Loosen tight collars, ties, and belts.",
                "Encourage slow, deep, calm breaths.",
                "If coughing up black soot or hoarseness is present, monitor for swelling of the vocal cords."
            ),
            criticalDonts = listOf(
                "NEVER allow the victim to lie flat if struggling for air.",
                "NEVER ignore mild symptoms; airway swelling can progress rapidly hours after exposure."
            ),
            whenToSeekEvacuation = "Stridor (harsh high-pitched breathing sound), persistent cough, burns around the mouth or nostrils, or dizziness/confusion."
        )
    )
}
