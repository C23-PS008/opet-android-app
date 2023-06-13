package com.c23ps008.opet.ui.screen.find_match_cat

data class FindMatchCatQuestion(
    val question: String,
    val options: List<Options>,
)

data class Options(
    val label: String,
    val value: Int,
)

val findMatchCatQuestions = listOf(
    FindMatchCatQuestion("How do you like cats that are family friendly?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like shedding cats?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like cats that have good general health?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like active and playful cats?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like cats that are child friendly?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like cats that need regular grooming?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like cats that have high intelligence?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    ),
    FindMatchCatQuestion("How do you like cats who are friendly to other pets?",
        listOf(
            Options("Very much", 5),
            Options("Somewhat", 4),
            Options("Neutral", 3),
            Options("Not much", 2),
            Options("Not at all", 1)
        )
    )
)
