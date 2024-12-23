package com.soloscape.home.presentations.write

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.model.Write
import com.soloscape.database.domain.usecase.WriteUseCases
import com.soloscape.home.presentations.write.components.WriteEvent
import com.soloscape.home.presentations.write.components.WriteTextFieldState
import com.soloscape.ui.Mood
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class WriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val writeUseCases: WriteUseCases
) : ViewModel() {

    private val _noteTitle = mutableStateOf(
        WriteTextFieldState(
        hint = "Enter title..."
    )
    )
    val noteTitle : State<WriteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        WriteTextFieldState(
        hint = "Enter some content..."
    )
    )
    val noteContent : State<WriteTextFieldState> = _noteContent

    private var currentNoteId : Int? = null

    var uiState by mutableStateOf(WriteTextFieldState())
        private set

    init {
        savedStateHandle.get<Int>("noteId")?.let { writeId ->
            if (writeId != -1) {
                viewModelScope.launch {
                    writeUseCases.getWriteById(writeId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: WriteEvent) {
        when (event) {
            is WriteEvent.UpsertWriteItem -> {
                upsertWriteItem(event.onSuccess)
            }

            is WriteEvent.DeleteWriteItem -> {
                deleteCartItem(event.writeItem)
            }

            is WriteEvent.DeleteAllWriteItem -> {
            }

            is WriteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is WriteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is WriteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }
            is WriteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank())
            }
        }
    }

    private fun upsertWriteItem(onSuccess :() -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.addWrite(write = Write(
            title = noteTitle.value.text,
            content = noteContent.value.text,
            mood = Mood.Happy.name,
            id = currentNoteId,))

        withContext(Dispatchers.Main){
            onSuccess()
        }
    }

    private fun deleteCartItem(write: Write) = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.deleteWrite(write = write)
    }


//    @SuppressLint("NewApi")
//    fun updateDateTime(zonedDateTime: ZonedDateTime) {
//        uiState = uiState.copy(updatedDateTime = zonedDateTime)
//    }

//    fun insertUpdateNotes(
//        report: Write,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit,
//    ) {
//        viewModelScope.launch(Dispatchers.Main) {
//            if (uiState.selectedReportId != null) {
//                updateNotes(report = report, onSuccess = onSuccess, onError = onError)
//            } else {
//                insertNotes(report = report, onSuccess = onSuccess, onError = onError)
//            }
//        }
//    }

//    private suspend fun insertNotes(
//        report: Write,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit,
//    ) {
//        val result = MongoDB.addNewNotes(
//            report = report.apply {
//                if (uiState.updatedDateTime != null) {
//                    date = uiState.updatedDateTime!!
//                }
//            },
//        )
//        if (result is RequestState.Success) {
//
//            withContext(Dispatchers.Main) {
//                onSuccess()
//            }
//        } else if (result is com.soloscape.model.RequestState.Error) {
//            withContext(Dispatchers.Main) {
//                onError(result.error.message.toString())
//            }
//        }
//    }
//
//    private suspend fun updateNotes(
//        report: com.soloscape.database.model.Write,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit,
//    ) {
//        val result = MongoDB.updateNotes(
//            report = report.apply {
//                _id = ObjectId.invoke(uiState.selectedReportId!!)
//                date =
//                    if (uiState.updatedDateTime != null) uiState.updatedDateTime!! else uiState.selectedReport!!.date
//            },
//        )
//        if (result is RequestState.Success) {
//
//            withContext(Dispatchers.Main) {
//                onSuccess()
//            }
//        } else if (result is RequestState.Error) {
//            withContext(Dispatchers.Main) {
//                onError(result.error.message.toString())
//            }
//        }
//    }
//
//    fun deleteNotes(
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit,
//    ) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (uiState.selectedReportId != null) {
//                val result = MongoDB.deleteNotes(id = ObjectId.invoke(uiState.selectedReportId!!))
//                if (result is com.soloscape.model.RequestState.Success) {
//                    withContext(Dispatchers.Main) {
//                        uiState.selectedReport?.let { deleteImagesFromFirebase(images = it.images) }
//                        onSuccess()
//                    }
//                } else if (result is com.soloscape.model.RequestState.Error) {
//                    withContext(Dispatchers.Main) {
//                        onError(result.error.message.toString())
//                    }
//                }
//            }
//        }
//    }

}
