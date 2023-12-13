package com.compose.report.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.compose.report.data.repository.MongoDB
import com.compose.report.navigation.Screen
import com.compose.report.navigation.SetupNavGraph
import com.compose.report.ui.theme.MultiModularArchJCTheme
import com.compose.report.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {

    var keepSplashOpened = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{ keepSplashOpened }
        WindowCompat.setDecorFitsSystemWindows(window,false)
        setContent {
            MultiModularArchJCTheme{
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navHostController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    })
            }
        }
    }
}

private fun getStartDestination() : String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route
}

