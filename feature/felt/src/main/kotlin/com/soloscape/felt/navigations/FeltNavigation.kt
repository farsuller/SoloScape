package com.soloscape.felt.navigations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.soloscape.felt.presentations.felt.FeltScreen
import com.soloscape.felt.presentations.felt.FeltViewModel
import com.soloscape.felt.presentations.felt.components.FeltEvent
import com.soloscape.ui.R
import com.soloscape.ui.components.DisplayAlertDialog
import com.soloscape.util.routes.ScreensRoutes

fun NavGraphBuilder.feltRoute(
    navigationToWrite: () -> Unit,
    navigateToWriteWithArgs: (Int?) -> Unit,
    onBackPressed: () -> Unit,
) {
    composable(route = ScreensRoutes.FeltRoute.route) {
        val viewModel: FeltViewModel = hiltViewModel()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()

        var deleteAllDialogOpened by remember { mutableStateOf(false) }

        FeltScreen(
            homeState = homeState,
            navigateToWrite = navigationToWrite,
            navigateToWriteWithArgs = navigateToWriteWithArgs,
            dateIsSelected = viewModel.dateIsSelected,
            onDateSelected = { viewModel.onEvent(FeltEvent.FilterBySelectedDate(dateTime = it)) },
            onDateReset = { viewModel.onEvent(FeltEvent.DisplayAllDate) },
            onBackPressed = onBackPressed,
            onDeleteAllConfirmed = { viewModel.onEvent(FeltEvent.DeleteAll) },
        )

        DisplayAlertDialog(
            title = stringResource(R.string.delete_title),
            message = stringResource(R.string.delete_content),
            dialogOpened = deleteAllDialogOpened,
            onCloseDialog = { deleteAllDialogOpened = false },
            onYesClicked = {
            },
        )
    }
}
