package com.oyenscilik.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Letters : Screen("letters")
    object LetterDetail : Screen("letter/{letterId}") {
        fun createRoute(letterId: Int) = "letter/$letterId"
    }
    object Numbers : Screen("numbers")
object NumberDetail : Screen("number/{numberId}") {
    fun createRoute(numberId: Int) = "number/$numberId"
}

    object Animals : Screen("animals")
object AnimalDetail : Screen("animal/{animalId}") {
    fun createRoute(animalId: Int) = "animal/$animalId"
}

    object Profile : Screen("profile")
object MonthlyStreak : Screen("streak")

object Leaderboard : Screen("leaderboard")

}
