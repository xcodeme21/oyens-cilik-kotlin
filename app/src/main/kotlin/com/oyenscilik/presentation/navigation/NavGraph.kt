package com.oyenscilik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.oyenscilik.presentation.screens.home.HomeScreen
import com.oyenscilik.presentation.screens.letters.LetterDetailScreen
import com.oyenscilik.presentation.screens.letters.LettersScreen
import com.oyenscilik.presentation.screens.splash.SplashScreen
import com.oyenscilik.presentation.screens.numbers.NumbersScreen
import com.oyenscilik.presentation.screens.numbers.NumberDetailScreen
import com.oyenscilik.presentation.screens.animals.AnimalsScreen
import com.oyenscilik.presentation.screens.animals.AnimalDetailScreen
import com.oyenscilik.presentation.screens.profile.ProfileScreen
import com.oyenscilik.presentation.screens.profile.MonthlyStreakScreen
import com.oyenscilik.presentation.screens.leaderboard.LeaderboardScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    viewModel: com.oyenscilik.presentation.MainViewModel
) {
    val isGuestLimitReached by viewModel.isGuestLimitReached.collectAsState()
    val isGuestMode by viewModel.isGuestMode.collectAsState()

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

        composable(Screen.Login.route) {
            com.oyenscilik.presentation.screens.auth.LoginScreen(
                onLoginSuccess = {
                    viewModel.onLoginSuccess()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            com.oyenscilik.presentation.screens.auth.RegisterScreen(
                onRegisterSuccess = {
                    viewModel.onLoginSuccess()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLetters = {
                    if (isGuestMode && isGuestLimitReached) {
                         navController.navigate(Screen.Login.route)
                    } else {
                         viewModel.onLessonCompleted() // Increment count on start for simplicity
                         navController.navigate(Screen.Letters.route)
                    }
                },
                onNavigateToNumbers = {
                    if (isGuestMode && isGuestLimitReached) {
                         navController.navigate(Screen.Login.route)
                    } else {
                         viewModel.onLessonCompleted()
                         navController.navigate(Screen.Numbers.route)
                    }
                },
                onNavigateToAnimals = {
                    if (isGuestMode && isGuestLimitReached) {
                         navController.navigate(Screen.Login.route)
                    } else {
                         viewModel.onLessonCompleted()
                         navController.navigate(Screen.Animals.route)
                    }
                }
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
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString("letterId")?.toIntOrNull() ?: 1
            LetterDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigatePrev = {
                    val prevId = letterId - 1
                    if (prevId >= 1) {
                        navController.navigate(Screen.LetterDetail.createRoute(prevId)) {
                            popUpTo(Screen.LetterDetail.route) { inclusive = true }
                        }
                    }
                },
                onNavigateNext = {
                    val nextId = letterId + 1
                    if (nextId <= 26) {
                        navController.navigate(Screen.LetterDetail.createRoute(nextId)) {
                            popUpTo(Screen.LetterDetail.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Numbers.route) {
            NumbersScreen(
                onNavigateToNumber = { id ->
                    navController.navigate(Screen.NumberDetail.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.NumberDetail.route,
            arguments = listOf(
                navArgument("numberId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val numberId = backStackEntry.arguments?.getInt("numberId") ?: 0
            NumberDetailScreen(
                numberId = numberId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Animals.route) {
            AnimalsScreen(
                onNavigateToAnimal = { id ->
                    navController.navigate(Screen.AnimalDetail.createRoute(id))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AnimalDetail.route,
            arguments = listOf(
                navArgument("animalId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getInt("animalId") ?: 1
            AnimalDetailScreen(
                animalId = animalId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStreak = { navController.navigate(Screen.MonthlyStreak.route) }
            )
        }

        composable(Screen.MonthlyStreak.route) {
            MonthlyStreakScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
