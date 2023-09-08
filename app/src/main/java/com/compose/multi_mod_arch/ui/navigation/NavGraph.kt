package com.compose.multi_mod_arch.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.compose.multi_mod_arch.ui.presentation.screens.auth.AuthenticationScreen
import com.compose.multi_mod_arch.util.Constants.REPORT_SCREEN_ARG_KEY
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(startDestination: String, navHostController: NavHostController){
    NavHost(
        navController = navHostController,
        startDestination = startDestination)
    {
        authenticationRoute()
        homeRoute()
        reportRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(){
    composable(route = Screen.Authentication.route){

        val oneTapState = rememberOneTapSignInState()

        AuthenticationScreen(
            loadingState = oneTapState.opened,
            oneTapState = oneTapState,
            onButtonClicked = {
                oneTapState.open()
            }
        )
    }
}

fun NavGraphBuilder.homeRoute(){
    composable(route = Screen.Home.route){

    }
}

fun NavGraphBuilder.reportRoute(){
    composable(
        route = Screen.Report.route,
        arguments = listOf(navArgument(name = REPORT_SCREEN_ARG_KEY){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ){

    }
}