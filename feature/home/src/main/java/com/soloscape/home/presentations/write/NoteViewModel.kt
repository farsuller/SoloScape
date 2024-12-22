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
import com.soloscape.ui.GalleryState
import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY
import com.soloscape.ui.Mood
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NoteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val writeUseCases: WriteUseCases
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle : State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content..."
    ))
    val noteContent : State<NoteTextFieldState> = _noteContent

    var uiState by mutableStateOf(WriteState())
        private set

    init {
        getReportIdArgument()
        fetchSelectedReport()
    }

    fun onEvent(event: WriteEvent) {
        when (event) {
            is WriteEvent.UpsertWriteItem -> {
                upsertWriteItem(event.writeItem)
            }

            is WriteEvent.DeleteWriteItem -> {
                deleteCartItem(event.writeItem)
            }

            is WriteEvent.DeleteAllWriteItem -> {
            }
        }
    }

    private fun upsertWriteItem(write: Write) = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.addWrite(write = write)
    }

    private fun deleteCartItem(write: Write) = viewModelScope.launch(Dispatchers.IO) {
        writeUseCases.deleteWrite(write = write)
    }

    private fun getReportIdArgument() {
        uiState = uiState.copy(
            selectedReportId = savedStateHandle.get<String>(key = NOTE_SCREEN_ARG_KEY),
        )
    }

    private fun fetchSelectedReport() {
        if (uiState.selectedReportId != null) {
            viewModelScope.launch(Dispatchers.Main) {
//                MongoDB.getSelectedNotes(reportId = ObjectId.invoke(uiState.selectedReportId!!))
//                    .catch {
//                        emit(com.soloscape.model.RequestState.Error(Exception("Report is already deleted.")))
//                    }.collect { report ->
//                        if (report is com.soloscape.model.RequestState.Success) {
//                            setSelectedReport(report = report.data)
//                            setTitle(title = report.data.title)
//                            setDescription(description = report.data.description)
//                            setMood(mood = Mood.valueOf(report.data.mood))
//
//                            fetchImagesFromFirebase(
//                                remoteImagePaths = report.data.images,
//                                onImageDownload = { downloadedImage ->
//                                    galleryState.addImage(
//                                        GalleryImage(
//                                            image = downloadedImage,
//                                            remoteImagePath = extractImagePath(fullImageUrl = downloadedImage.toString()),
//                                        ),
//                                    )
//                                },
//                            )
//                        }
//                    }
            }
        }
    }
    fun refreshView() {
        fetchSelectedReport()
    }

    private fun setSelectedReport(report: Write) {
        uiState = uiState.copy(selectedReport = report)
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
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
