package com.soloscape.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.dashboard.model.JournalNoteItem
import com.soloscape.database.data.local.YourNameManager
import com.soloscape.database.domain.usecase.NotesUseCases
import com.soloscape.database.domain.usecase.WriteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val yourNameManager: YourNameManager,
    private val writeUseCases: WriteUseCases,
    private val notesUseCases: NotesUseCases,
) : ViewModel() {

    private val _yourNameState = MutableStateFlow(YourNameState())
    val yourNameState: StateFlow<YourNameState> = _yourNameState.asStateFlow()

    private val _journalNoteState = MutableStateFlow(JournalNoteState())
    val journalNoteState: StateFlow<JournalNoteState> = _journalNoteState.asStateFlow()

    init {
        _yourNameState.update { it.copy(text = yourNameManager.yourName) }

        getJournalList()
        getNoteList()
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

    private fun getJournalList() {
        writeUseCases.getWrite().onEach { write ->
            _journalNoteState.update { it.copy(journalList = write.values.flatten()) }
            combineNotesAndJournals()
        }.launchIn(viewModelScope)
    }

    private fun getNoteList() {
        notesUseCases.getNotes().onEach { notes ->
            _journalNoteState.update { it.copy(notesList = notes) }
            combineNotesAndJournals()
        }.launchIn(viewModelScope)
    }

    private fun combineNotesAndJournals() {
        val notes = _journalNoteState.value.notesList?.map { JournalNoteItem.NoteItem(it) }?.take(5) ?: emptyList()
        val journals = _journalNoteState.value.journalList?.map { JournalNoteItem.WriteItem(it) }?.take(5) ?: emptyList()

        val combinedList = (notes + journals).sortedBy {
            when (it) {
                is JournalNoteItem.NoteItem -> it.note.timestamp
                is JournalNoteItem.WriteItem -> it.write.date
            }
        }.shuffled()

        _journalNoteState.update { it.copy(combinedList = combinedList) }
    }
}
