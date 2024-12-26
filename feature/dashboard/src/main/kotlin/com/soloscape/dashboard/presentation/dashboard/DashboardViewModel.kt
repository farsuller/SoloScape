package com.soloscape.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.soloscape.dashboard.presentation.dashboard.components.YourNameEvent
import com.soloscape.dashboard.presentation.dashboard.components.YourNameState
import com.soloscape.database.data.local.YourNameManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val yourNameManager: YourNameManager,
) : ViewModel() {

    private val _yourNameState = MutableStateFlow(YourNameState())
    val yourNameState: StateFlow<YourNameState> = _yourNameState.asStateFlow()

    init {
        _yourNameState.update { it.copy(text = yourNameManager.yourName) }
    }

    fun onEvent(event: YourNameEvent) {
        when (event) {
            is YourNameEvent.EnteredYourName -> {
                _yourNameState.update { it.copy(text = event.value) }
                yourNameManager.yourName = event.value
            }

            is YourNameEvent.ChangeTextFocus -> {
                _yourNameState.update { it.copy(isHintVisible = !event.focusState.isFocused && it.text.isBlank()) }
            }
        }
    }
}
