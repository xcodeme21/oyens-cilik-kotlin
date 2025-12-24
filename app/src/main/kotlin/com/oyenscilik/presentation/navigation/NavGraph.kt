package com.oyenscilik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oyenscilik.presentation.screens.home.HomeScreen
import com.oyenscilik.presentation.screens.letters.LetterDetailScreen
import com.oyenscilik.presentation.screens.letters.LettersScreen
import com.oyenscilik.presentation.screens.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLetters = { navController.navigate(Screen.Letters.route) },
                onNavigateToNumbers = { navController.navigate(Screen.Numbers.route) },
                onNavigateToAnimals = { navController.navigate(Screen.Animals.route) }
            )
        }

        composable(Screen.Letters.route) {
            LettersScreen(
                onNavigateToLetter = { id ->
                    navController.navigate(Screen.LetterDetail.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.LetterDetail.route,
            arguments = listOf(
                navArgument("letterId") {
                    type = NavType.StringType
                }
            )
        ) {
            LetterDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Placeholder screens for Numbers and Animals
        composable(Screen.Numbers.route) {
            // TODO: NumbersScreen
        }

        composable(Screen.Animals.route) {
            // TODO: AnimalsScreen
        }
    }
}
