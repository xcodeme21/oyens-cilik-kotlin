package com.oyenscilik.app.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oyenscilik.app.data.audio.AudioPlayerManager
import com.oyenscilik.app.presentation.screens.animals.AnimalDetailScreen
import com.oyenscilik.app.presentation.screens.animals.AnimalQuizScreen
import com.oyenscilik.app.presentation.screens.animals.AnimalsScreen
import com.oyenscilik.app.presentation.screens.auth.LoginScreen
import com.oyenscilik.app.presentation.screens.auth.RegisterScreen
import com.oyenscilik.app.presentation.screens.home.HomeScreen
import com.oyenscilik.app.presentation.screens.letters.LetterDetailScreen
import com.oyenscilik.app.presentation.screens.letters.LettersScreen
import com.oyenscilik.app.presentation.screens.numbers.NumberDetailScreen
import com.oyenscilik.app.presentation.screens.numbers.NumbersScreen
import com.oyenscilik.app.presentation.screens.onboarding.OnboardingScreen
import com.oyenscilik.app.presentation.screens.profile.ProfileScreen

@Composable
fun OyensCilikNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    audioPlayerManager: AudioPlayerManager
) {
    Box(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // Changed from Onboarding to Home
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
        // Onboarding & Auth
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLetters = { navController.navigate(Screen.Letters.route) },
                onNavigateToNumbers = { navController.navigate(Screen.Numbers.route) },
                onNavigateToAnimals = { navController.navigate(Screen.Animals.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        
        // Letters
        composable(Screen.Letters.route) {
            LettersScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLetter = { letterId ->
                    navController.navigate(Screen.LetterDetail.createRoute(letterId))
                }
            )
        }
        
        composable(
            route = Screen.LetterDetail.route,
            arguments = listOf(navArgument("letterId") { type = NavType.IntType })
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getInt("letterId") ?: 1
            LetterDetailScreen(
                letterId = letterId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = {
                    if (letterId < 26) {
                        navController.navigate(Screen.LetterDetail.createRoute(letterId + 1)) {
                            popUpTo(Screen.Letters.route)
                        }
                    }
                },
                onNavigatePrevious = {
                    if (letterId > 1) {
                        navController.navigate(Screen.LetterDetail.createRoute(letterId - 1)) {
                            popUpTo(Screen.Letters.route)
                        }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                audioPlayerManager = audioPlayerManager
            )
        }
        
        // Numbers
        composable(Screen.Numbers.route) {
            NumbersScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNumber = { numberId ->
                    navController.navigate(Screen.NumberDetail.createRoute(numberId))
                }
            )
        }
        
        composable(
            route = Screen.NumberDetail.route,
            arguments = listOf(navArgument("numberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val numberId = backStackEntry.arguments?.getInt("numberId") ?: 0
            NumberDetailScreen(
                numberId = numberId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = {
                    if (numberId < 20) {
                        navController.navigate(Screen.NumberDetail.createRoute(numberId + 1)) {
                            popUpTo(Screen.Numbers.route)
                        }
                    }
                },
                onNavigatePrevious = {
                    if (numberId > 0) {
                        navController.navigate(Screen.NumberDetail.createRoute(numberId - 1)) {
                            popUpTo(Screen.Numbers.route)
                        }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                audioPlayerManager = audioPlayerManager
            )
        }
        
        // Animals
        composable(Screen.Animals.route) {
            AnimalsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAnimal = { animalId ->
                    navController.navigate(Screen.AnimalDetail.createRoute(animalId))
                },
                onNavigateToQuiz = { navController.navigate(Screen.AnimalQuiz.route) }
            )
        }
        
        composable(
            route = Screen.AnimalDetail.route,
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getInt("animalId") ?: 1
            AnimalDetailScreen(
                animalId = animalId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                audioPlayerManager = audioPlayerManager
            )
        }
        
        composable(Screen.AnimalQuiz.route) {
            AnimalQuizScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Profile
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        }
    }
}
