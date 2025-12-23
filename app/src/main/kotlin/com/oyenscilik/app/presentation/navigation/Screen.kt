package com.oyenscilik.app.presentation.navigation

sealed class Screen(val route: String) {
    // Onboarding & Auth
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    
    // Main
    object Home : Screen("home")
    object SelectChild : Screen("select_child")
    object AddChild : Screen("add_child")
    
    // Learning - Letters
    object Letters : Screen("letters")
    object LetterDetail : Screen("letter/{letterId}") {
        fun createRoute(letterId: Int) = "letter/$letterId"
    }
    object LetterQuiz : Screen("letter_quiz")
    
    // Learning - Numbers
    object Numbers : Screen("numbers")
    object NumberDetail : Screen("number/{numberId}") {
        fun createRoute(numberId: Int) = "number/$numberId"
    }
    object NumberQuiz : Screen("number_quiz")
    
    // Learning - Animals
    object Animals : Screen("animals")
    object AnimalDetail : Screen("animal/{animalId}") {
        fun createRoute(animalId: Int) = "animal/$animalId"
    }
    object AnimalQuiz : Screen("animal_quiz")
    
    // Profile & Settings
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
