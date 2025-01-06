package com.soloscape.felt.presentations.felt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.usecase.JournalUseCases
import com.soloscape.felt.presentations.felt.components.JournalEvent
import com.soloscape.felt.presentations.felt.components.JournalState
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
class JournalViewModel @Inject constructor(
    private val writeUseCases: JournalUseCases,
) : ViewModel() {

    private val _journalState = MutableStateFlow(JournalState())
    val journalState: StateFlow<JournalState> = _journalState.asStateFlow()

    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getJournalList()
    }

    fun onEvent(event: JournalEvent) {
        when (event) {
            is JournalEvent.DisplayAllDate -> getJournalList()
            is JournalEvent.FilterBySelectedDate -> {
                getWriteFilteredList(zonedDateTime = event.dateTime)
            }
            JournalEvent.DeleteAll -> deleteAllWrite()
        }
    }

    private fun getJournalList() {
        dateIsSelected = false

        writeUseCases.getJournals().onEach { write ->
            _journalState.update { it.copy(writes = write) }
        }.launchIn(viewModelScope)
    }

    private fun getWriteFilteredList(zonedDateTime: ZonedDateTime) {
        dateIsSelected = true

        writeUseCases.getJournalByFiltered(date = zonedDateTime).onEach { write ->
            _journalState.update { it.copy(writes = write) }
        }.launchIn(viewModelScope)
    }

    private fun deleteAllWrite() = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.deleteAllJournal()
    }
}
