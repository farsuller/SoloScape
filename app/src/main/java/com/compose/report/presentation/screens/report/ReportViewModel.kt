package com.compose.report.presentation.screens.report

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.report.data.repository.MongoDB
import com.compose.report.model.Mood
import com.compose.report.model.Report
import com.compose.report.util.Constants
import com.compose.report.util.Constants.REPORT_SCREEN_ARG_KEY
import com.compose.report.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.mongodb.kbson.ObjectId

class ReportViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getReportIdArgument()
        fetchSelectedReport()
    }

    private fun getReportIdArgument(){
        uiState = uiState.copy(
            selectedReportId = savedStateHandle.get<String>(key = REPORT_SCREEN_ARG_KEY)
        )
    }

    private fun fetchSelectedReport(){
        if(uiState.selectedReportId != null){
            viewModelScope.launch(Dispatchers.Main) {
               MongoDB.getSelectedReport(reportId = ObjectId.invoke(uiState.selectedReportId!!))
                   .catch {
                       emit(RequestState.Error(Exception("Report is already deleted.")))
                   }.collect{ report ->
                       if(report is RequestState.Success){
                           setSelectedReport(report = report.data)
                           setTitle(title = report.data.title)
                           setDescription(description = report.data.description)
                           setMood(mood = Mood.valueOf(report.data.mood))
                       }
               }



            }
        }
    }

    fun setSelectedReport(report: Report){
        uiState = uiState.copy(selectedReport = report)
    }

    fun setTitle(title :String){
        uiState = uiState.copy(title = title)
    }
    fun setDescription(description: String){
        uiState = uiState.copy(description = description)
    }

    fun setMood(mood :Mood){
        uiState = uiState.copy(mood = mood)
    }

}

data class UiState(
    val selectedReportId: String? = null,
    val selectedReport : Report? = null,
    val title: String = "",
    val description : String = "",
    val mood : Mood = Mood.Neutral
)