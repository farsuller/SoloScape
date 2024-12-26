package com.compose.soloscape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.compose.soloscape.navigation.SetupNavGraph
import com.google.firebase.FirebaseApp
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.routes.ScreensRoutes
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
            SoloScapeTheme(
                dynamicColor = false,
            ) {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = ScreensRoutes.DashboardRoute.route,
                    navHostController = navController,
                    onDataLoaded = { keepSplashOpened = it },
                )
            }
        }
    }
}
