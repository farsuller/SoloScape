package com.soloscape.idea.presentations.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.usecase.NotesUseCases
import com.soloscape.idea.presentations.note.components.NoteEvent
import com.soloscape.idea.presentations.note.components.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NotesUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _noteState = MutableStateFlow(NoteState())
    val noteState: StateFlow<NoteState> = _noteState.asStateFlow()

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        _noteState.update {
                            it.copy(
                                id = note.id,
                                title = note.title,
                                titleHintVisible = false,
                                content = note.content,
                                contentHintVisible = false,
                                noteColor = note.color,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.EnteredTitle -> {
                _noteState.update { it.copy(title = event.value) }
            }

            is NoteEvent.ChangeTitleFocus -> {
                _noteState.update { it.copy(titleHintVisible = !event.focusState.isFocused && it.title?.isBlank() == true) }
            }

            is NoteEvent.EnteredContent -> {
                _noteState.update { it.copy(content = event.value) }
            }

            is NoteEvent.ChangeContentFocus -> {
                _noteState.update { it.copy(contentHintVisible = !event.focusState.isFocused && it.content?.isBlank() == true) }
            }

            is NoteEvent.ChangeColor -> {
                _noteState.update { it.copy(noteColor = event.color) }
            }

            is NoteEvent.SaveNote -> {
                addEditSaveNote(onSuccess = event.onSuccess)
            }

            is NoteEvent.DeleteNoteItem -> {}
        }
    }

    private fun addEditSaveNote(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val state = noteState.value
        noteUseCases.addNote(
            Note(
                id = state.id,
                title = state.title,
                content = state.content,
                timestamp = System.currentTimeMillis(),
                color = state.noteColor,
            ),
        )

        withContext(Dispatchers.Main) {
            onSuccess()
        }
    }

    private fun deleteNoteItem(note: Note, onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        noteUseCases.deleteNote(note = note)

        withContext(Dispatchers.Main) {
            onSuccess()
        }
    }
}
