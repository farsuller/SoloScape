package com.soloscape.home.navigation

import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.home.HomeScreen
import com.soloscape.home.HomeViewModel
import com.soloscape.mongo.repository.MongoDB
import com.soloscape.ui.components.DisplayAlertDialog
import com.soloscape.util.Constants.APP_ID
import com.soloscape.util.ScreensRoutes
import com.soloscape.util.model.RequestState
import io.realm.kotlin.mongodb.App

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavGraphBuilder.homeRoute(
    navigationToWrite: () -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit
) {
    composable(route = ScreensRoutes.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        val report by viewModel.reports
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }
        val context = LocalContext.current


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
            },
            onDeleteAllClicked = {
                deleteAllDialogOpened = true
            },
            dateIsSelected = viewModel.dateIsSelected,
            onDateSelected = { viewModel.getReports(zonedDateTime = it) },
            onDateReset = { viewModel.getReports() },
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated,

        )


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

        DisplayAlertDialog(
            title = "Delete all notes",
            message = "Are you sure you want to delete all notes?",
            dialogOpened = deleteAllDialogOpened,
            onCloseDialog = { deleteAllDialogOpened = false },
            onYesClicked = {
                viewModel.deleteAllReports(
                    onSuccess = {
                        Toast.makeText(context, "All Notes Deleted", Toast.LENGTH_SHORT).show()
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            if (it.message == "No Internet Connection.") "We need internet connection to delete all notes." else it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            })
    }
}