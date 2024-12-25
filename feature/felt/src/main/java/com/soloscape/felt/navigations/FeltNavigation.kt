package com.soloscape.felt.navigations

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.felt.presentations.felt.FeltScreen
import com.soloscape.felt.presentations.felt.FeltViewModel
import com.soloscape.ui.components.DisplayAlertDialog
import com.soloscape.util.routes.ScreensRoutes
import kotlinx.coroutines.launch

fun NavGraphBuilder.feltRoute(
    navigationToWrite: () -> Unit,
    navigateToWriteWithArgs: (Int?) -> Unit,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
) {
    composable(route = ScreensRoutes.FeltRoute.route) {
        val viewModel: FeltViewModel = hiltViewModel()
        val scope = rememberCoroutineScope()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        var deleteAllDialogOpened by remember { mutableStateOf(false) }

        FeltScreen(
            homeState = homeState,
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
            onDateSelected = { viewModel.getWrite(zonedDateTime = it) },
            onDateReset = { viewModel.getWrite() },
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated,
        )

        DisplayAlertDialog(
            title = "Delete all notes",
            message = "Are you sure you want to delete all notes?",
            dialogOpened = deleteAllDialogOpened,
            onCloseDialog = { deleteAllDialogOpened = false },
            onYesClicked = {
//                viewModel.deleteAllReports(
//                    onSuccess = {
//                        Toast.makeText(context, "All Notes Deleted", Toast.LENGTH_SHORT).show()
//                        scope.launch {
//                            drawerState.close()
//                        }
//                    },
//                    onError = {
//                        Toast.makeText(
//                            context,
//                            if (it.message == "No Internet Connection.") "We need internet connection to delete all notes." else it.message,
//                            Toast.LENGTH_SHORT,
//                        ).show()
//                        scope.launch {
//                            drawerState.close()
//                        }
//                    },
//                )
            },
        )
    }
}
