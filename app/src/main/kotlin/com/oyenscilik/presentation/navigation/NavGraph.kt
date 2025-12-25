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
import com.oyenscilik.presentation.screens.numbers.NumbersScreen
import com.oyenscilik.presentation.screens.numbers.NumberDetailScreen
import com.oyenscilik.presentation.screens.animals.AnimalsScreen
import com.oyenscilik.presentation.screens.animals.AnimalDetailScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLetters = {
                    navController.navigate(Screen.Letters.route)
                },
                onNavigateToNumbers = {
                    navController.navigate(Screen.Numbers.route)
                },
                onNavigateToAnimals = {
                    navController.navigate(Screen.Animals.route)
                }
            )
        }

        composable(Screen.Letters.route) {
            LettersScreen(
                onNavigateToLetter = { id ->
                    navController.navigate(Screen.LetterDetail.createRoute(id))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
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
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.NumberDetail.route,
            arguments = listOf(
                navArgument("numberId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val numberId = backStackEntry.arguments?.getString("numberId")?.toIntOrNull() ?: 0
            NumberDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigatePrev = {
                    val prevId = numberId - 1
                    if (prevId >= 0) {
                        navController.navigate(Screen.NumberDetail.createRoute(prevId)) {
                            popUpTo(Screen.NumberDetail.route) { inclusive = true }
                        }
                    }
                },
                onNavigateNext = {
                    val nextId = numberId + 1
                    if (nextId <= 20) {
                        navController.navigate(Screen.NumberDetail.createRoute(nextId)) {
                            popUpTo(Screen.NumberDetail.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Animals.route) {
            AnimalsScreen(
                onNavigateToAnimal = { id ->
                    navController.navigate(Screen.AnimalDetail.createRoute(id))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.AnimalDetail.route,
            arguments = listOf(
                navArgument("animalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId")?.toIntOrNull() ?: 1
            AnimalDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigatePrev = {
                    val prevId = animalId - 1
                    if (prevId >= 1) {
                        navController.navigate(Screen.AnimalDetail.createRoute(prevId)) {
                            popUpTo(Screen.AnimalDetail.route) { inclusive = true }
                        }
                    }
                },
                onNavigateNext = {
                    val nextId = animalId + 1
                    if (nextId <= 15) {
                        navController.navigate(Screen.AnimalDetail.createRoute(nextId)) {
                            popUpTo(Screen.AnimalDetail.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
