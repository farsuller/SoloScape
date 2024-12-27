package com.soloscape.idea.presentations.idea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.usecase.NotesUseCases
import com.soloscape.idea.presentations.idea.components.IdeaEvent
import com.soloscape.idea.presentations.idea.components.IdeaState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdeaViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
) : ViewModel() {

    private val _ideaState = MutableStateFlow(IdeaState())
    val ideaState: StateFlow<IdeaState> = _ideaState.asStateFlow()

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    fun onEvent(event: IdeaEvent) {
        when (event) {
            is IdeaEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    notesUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }

            is IdeaEvent.RestoreNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    notesUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
        }
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotes()
            .onEach { notes ->
                _ideaState.update { it.copy(notes = notes) }
            }
            .launchIn(viewModelScope)
    }
}
