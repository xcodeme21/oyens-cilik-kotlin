package com.oyenscilik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.oyenscilik.presentation.MainViewModel
import com.oyenscilik.presentation.components.PremiumBottomNavigation
import com.oyenscilik.presentation.navigation.Screen
import com.oyenscilik.presentation.navigation.NavGraph
import com.oyenscilik.presentation.theme.OyensCilikTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OyensCilikTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        val bottomBarRoutes = listOf(
                            Screen.Home.route,
                            Screen.Leaderboard.route,
                            Screen.Profile.route
                        )
                        
                        AnimatedVisibility(
                            visible = currentRoute in bottomBarRoutes,
                            enter = slideInVertically { it } + fadeIn(),
                            exit = slideOutVertically { it } + fadeOut()
                        ) {
                            PremiumBottomNavigation(navController = navController)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
