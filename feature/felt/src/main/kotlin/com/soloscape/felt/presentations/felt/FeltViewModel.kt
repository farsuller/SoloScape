package com.soloscape.felt.presentations.felt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.usecase.JournalUseCases
import com.soloscape.felt.presentations.felt.components.FeltEvent
import com.soloscape.felt.presentations.felt.components.FeltState
import com.soloscape.util.connectivity.ConnectivityObserver
import com.soloscape.util.connectivity.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
internal class FeltViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val writeUseCases: JournalUseCases,
) : ViewModel() {

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    private val _journalState = MutableStateFlow(FeltState())
    val journalState: StateFlow<FeltState> = _journalState.asStateFlow()

    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getWriteList()
    }

    fun onEvent(event: FeltEvent) {
        when (event) {
            is FeltEvent.DisplayAllDate -> getWriteList()
            is FeltEvent.FilterBySelectedDate -> {
                getWriteFilteredList(zonedDateTime = event.dateTime)
            }
            FeltEvent.DeleteAll -> deleteAllWrite()
        }
    }

    private fun getWriteList() {
        dateIsSelected = false

        writeUseCases.getWrite().onEach { write ->
            _journalState.update { it.copy(writes = write) }
        }.launchIn(viewModelScope)
    }

    private fun getWriteFilteredList(zonedDateTime: ZonedDateTime) {
        dateIsSelected = true

        writeUseCases.getWriteByFiltered(date = zonedDateTime).onEach { write ->
            _journalState.update { it.copy(writes = write) }
        }.launchIn(viewModelScope)
    }

    private fun deleteAllWrite() = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.deleteAllWrite()
    }
}
