package com.c23ps008.opet.ui.screen.find_match_dog

data class FindMatchDogQuestion(
    val question: String,
    val options: List<String>,
)

val findMatchDogQuestions = listOf(
    // Question 1
    FindMatchDogQuestion(
        question = "How easy do you train a dog?", options = listOf(
            "Easy Training", "May be Stubborn", "Eager to Please", "Independent", "Agreeable"
        )
    ),
    // Question 2
    FindMatchDogQuestion(
        question = "How high a level of energy do you want from your dog?", options = listOf(
            "Regular Exercise", "Energetic", "Needs Lots of Activity", "Couch Potato", "Calm"
        )
    ),// Question 3
    FindMatchDogQuestion(
        question = "How much fur are you willing to clean from your dog?", options = listOf(
            "Seasonal", "Infrequent", "Occasional", "Regularly", "Frequent"
        )
    ),// Question 4
    FindMatchDogQuestion(
        question = "How often do you groom your dog's fur?", options = listOf(
            "2-3 Times a Week Brushing",
            "Daily Brushing",
            "Occasional Bath/Brush",
            "Weekly Brushing",
            "Specialty/Professional"
        )
    ),// Question 5
    FindMatchDogQuestion(
        question = "What kind of personality do you want from your dog?", options = listOf(
            "Amiable",
            "Playful",
            "Loyal",
            "Confident",
            "Intelligent",
            "Gentle",
            "Active",
            "Low-Key",
            "Independent",
            "Brave"
        )
    ),// Question 6
    FindMatchDogQuestion(
        question = "How heavy a dog do you want?", options = listOf(
            "Light", "Medium", "Heavy", "Very Heavy"
        )
    ),// Question 7
    FindMatchDogQuestion(
        question = "How tall a dog do you want?", options = listOf(
            "Short", "Medium", "Tall", "Very Tall"
        )
    ),// Question 8
    FindMatchDogQuestion(
        question = "How do you want your dog to behave towards other people and animals?",
        options = listOf(
            "Outgoing", "Aloof/Wary", "Friendly", "Alert/Responsive", "Reserved with Strangers"
        )
    ),
)