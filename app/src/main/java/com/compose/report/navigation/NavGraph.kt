package com.compose.report.navigation

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.compose.report.data.repository.MongoDB
import com.compose.report.model.Mood
import com.compose.report.model.Report
import com.compose.report.presentation.components.DisplayAlertDialog
import com.compose.report.presentation.screens.auth.AuthenticationScreen
import com.compose.report.presentation.screens.auth.AuthenticationViewModel
import com.compose.report.presentation.screens.home.HomeScreen
import com.compose.report.presentation.screens.home.HomeViewModel
import com.compose.report.presentation.screens.report.ReportScreen
import com.compose.report.presentation.screens.report.ReportViewModel
import com.compose.report.util.Constants.APP_ID
import com.compose.report.util.Constants.REPORT_SCREEN_ARG_KEY
import com.compose.report.util.RequestState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

@Composable
fun SetupNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    onDataLoaded: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    )
    {
        authenticationRoute(
            navigateToHome = {
                navHostController.popBackStack()
                navHostController.navigate(Screen.Home.route)
            },
            onDataLoaded = onDataLoaded
        )
        homeRoute(
            navigationToWrite = {
                navHostController.navigate(Screen.Report.route)
            },
            navigateToWriteWithArgs = {
                navHostController.navigate(Screen.Report.passReportId(reportId = it))
            },
            navigateToAuth = {
                navHostController.popBackStack()
                navHostController.navigate(Screen.Authentication.route)
            },
            onDataLoaded = onDataLoaded
        )
        reportRoute(
            onBackPressed = {
                navHostController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated by viewModel.authenticated
        val loadingState by viewModel.loadingState
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            oneTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(
                    tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    }
                )

            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navigationToWrite: () -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val report by viewModel.reports
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = report) {
            if (report !is RequestState.Loading) {
                onDataLoaded()
            }
        }
        HomeScreen(
            reports = report,
            drawerState = drawerState,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
            navigateToWrite = navigationToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            onSignOutClicked = {
                signOutDialogOpened = true
            })


        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }


        DisplayAlertDialog(
            title = "Log Out",
            message = "Are you sure you want to Log out?",
            dialogOpened = signOutDialogOpened,
            onCloseDialog = { signOutDialogOpened = false },
            onYesClicked = {
                scope.launch(Dispatchers.IO) {
                    val user = App.create(APP_ID).currentUser

                    if (user != null) {
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            })
    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.reportRoute(onBackPressed: () -> Unit) {
    composable(
        route = Screen.Report.route,
        arguments = listOf(navArgument(name = REPORT_SCREEN_ARG_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val viewModel : ReportViewModel = viewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState()
        val pageNumber by remember {
            derivedStateOf{ pagerState.currentPage}
        }
        ReportScreen(
            onDeleteConfirmed = {},
            onBackPressed = onBackPressed,
            pagerState = pagerState,
            uiState = uiState,
            onTitleChanged = {viewModel.setTitle(title = it)},
            onDescriptionChanged = {viewModel.setDescription(description = it)},
            moodName = { Mood.values()[pageNumber].name}
        )
    }
}