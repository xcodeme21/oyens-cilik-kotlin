package com.oyenscilik.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Letters : Screen("letters")
    object LetterDetail : Screen("letter/{letterId}") {
        fun createRoute(letterId: Int) = "letter/$letterId"
    }
    object Numbers : Screen("numbers")
    object Animals : Screen("animals")
    object Profile : Screen("profile")
}
