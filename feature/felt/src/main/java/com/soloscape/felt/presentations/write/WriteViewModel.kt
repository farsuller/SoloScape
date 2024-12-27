package com.soloscape.felt.presentations.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.model.Write
import com.soloscape.database.domain.usecase.WriteUseCases
import com.soloscape.felt.presentations.write.components.WriteEvent
import com.soloscape.felt.presentations.write.components.WriteState
import com.soloscape.ui.Reaction
import com.soloscape.util.Constants.Routes.WRITE_ID_ARG_KEY
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
internal class WriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val writeUseCases: WriteUseCases,
) : ViewModel() {

    private val _writeState = MutableStateFlow(WriteState())
    val writeState: StateFlow<WriteState> = _writeState.asStateFlow()

    init {
        savedStateHandle.get<Int>(WRITE_ID_ARG_KEY)?.let { writeId ->
            if (writeId != -1) {
                viewModelScope.launch {
                    writeUseCases.getWriteById(writeId)?.also { write ->
                        _writeState.update {
                            it.copy(
                                id = write.id,
                                title = write.title,
                                titleHintVisible = false,
                                content = write.content,
                                contentHintVisible = false,
                                date = write.date,
                                reaction = Reaction.valueOf(write.mood ?: Reaction.Neutral.name),
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: WriteEvent) {
        when (event) {
            is WriteEvent.UpsertWriteItem -> {
                upsertWriteItem(onSuccess = event.onSuccess)
                _writeState.update { it.copy(reaction = event.reaction) }
            }

            is WriteEvent.DeleteWriteItem -> {
                deleteWriteItem(write = event.writeItem, onSuccess = event.onSuccess)
            }

            is WriteEvent.EnteredTitle -> {
                _writeState.update { it.copy(title = event.value) }
            }

            is WriteEvent.ChangeTitleFocus -> {
                _writeState.update { it.copy(titleHintVisible = !event.focusState.isFocused && it.title.isBlank()) }
            }

            is WriteEvent.EnteredContent -> {
                _writeState.update { it.copy(content = event.value) }
            }

            is WriteEvent.ChangeContentFocus -> {
                _writeState.update { it.copy(contentHintVisible = !event.focusState.isFocused && it.content.isBlank()) }
            }

            is WriteEvent.SelectedDateTime -> {
                _writeState.update { it.copy(date = event.date ?: System.currentTimeMillis()) }
            }
        }
    }

    private fun upsertWriteItem(onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val state = writeState.value
        writeUseCases.addWrite(
            write = Write(
                id = state.id,
                title = state.title,
                content = state.content,
                mood = state.reaction.name,
                date = state.date,
            ),
        )

        withContext(Dispatchers.Main) {
            onSuccess()
        }
    }

    private fun deleteWriteItem(write: Write, onSuccess: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.deleteWrite(write = write)

        withContext(Dispatchers.Main) {
            onSuccess()
        }
    }
}
