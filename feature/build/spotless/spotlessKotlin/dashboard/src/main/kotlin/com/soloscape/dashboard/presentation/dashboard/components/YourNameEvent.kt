package com.soloscape.dashboard.presentation.dashboard.components

import androidx.compose.ui.focus.FocusState

sealed class YourNameEvent {
    data class EnteredYourName(val value: String) : YourNameEvent()
    data class ChangeTextFocus(val focusState: FocusState) : YourNameEvent()
}
