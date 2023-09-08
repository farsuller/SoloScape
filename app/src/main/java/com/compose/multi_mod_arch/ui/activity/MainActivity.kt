package com.compose.multi_mod_arch.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.compose.multi_mod_arch.ui.navigation.Screen
import com.compose.multi_mod_arch.ui.navigation.SetupNavGraph
import com.compose.multi_mod_arch.ui.theme.MultiModularArchJCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MultiModularArchJCTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = Screen.Authentication.route,
                    navHostController = navController)
            }
        }
    }
}

