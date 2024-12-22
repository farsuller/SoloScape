package com.compose.soloscape

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.compose.soloscape.navigation.SetupNavGraph
import com.google.firebase.FirebaseApp
import com.soloscape.ui.theme.MultiModularArchJCTheme
import com.soloscape.util.ScreensRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keepSplashOpened }
        enableEdgeToEdge()

        FirebaseApp.initializeApp(this)
        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            MultiModularArchJCTheme(
                darkTheme = darkTheme,
                dynamicColor = false,
            ) {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = ScreensRoutes.Home.route,
                    navHostController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    },
                    darkTheme = darkTheme,
                    onThemeUpdated = {
                        darkTheme = !darkTheme
                    },
                )
            }
        }
    }
}




