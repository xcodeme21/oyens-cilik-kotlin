package com.oyenscilik.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.oyenscilik.app.data.audio.AudioPlayerManager
import com.oyenscilik.app.presentation.navigation.OyensCilikNavHost
import com.oyenscilik.app.presentation.theme.OyensCilikTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge properly
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            OyensCilikTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                
                // Release audio resources when leaving composable
                DisposableEffect(Unit) {
                    onDispose {
                        audioPlayerManager.release()
                    }
                }
                
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    OyensCilikNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        snackbarHostState = snackbarHostState,
                        audioPlayerManager = audioPlayerManager
                    )
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Make sure audio is released when activity is destroyed
        if (::audioPlayerManager.isInitialized) {
            audioPlayerManager.release()
        }
    }
}
