package com.soloscape.home.presentations.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.database.domain.usecase.WriteUseCases
import com.soloscape.home.presentations.home.components.HomeState
import com.soloscape.util.connectivity.ConnectivityObserver
import com.soloscape.util.connectivity.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
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
internal class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val writeUseCases: WriteUseCases
) : ViewModel() {

    private lateinit var allReportsJob: Job
    private lateinit var filteredReportsJob: Job
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getWriteList()
        viewModelScope.launch {
            connectivity.observe().collect { network = it }
        }
    }

    fun getWrite(zonedDateTime: ZonedDateTime? = null) {
        dateIsSelected = zonedDateTime != null

        if (dateIsSelected && zonedDateTime != null) {
            getWriteFilteredList(zonedDateTime = zonedDateTime)
        } else {
            getWriteList()
        }
    }

    private fun getWriteList() {
        writeUseCases.getWrite().onEach { write ->
            _homeState.update { it.copy(writes = write) }
        }.launchIn(viewModelScope)
    }

    private fun getWriteFilteredList(zonedDateTime: ZonedDateTime) {
        writeUseCases.getWriteByFiltered(date = zonedDateTime).onEach { write ->
            _homeState.update { it.copy(writes = write) }
        }.launchIn(viewModelScope)
    }




    private fun observeAllReports() {
        allReportsJob = viewModelScope.launch {
            if (::filteredReportsJob.isInitialized) {
                filteredReportsJob.cancelAndJoin()
            }
//            MongoDB.getAllNotes().debounce(2000).collect { result ->
//                reports.value = result
//            }
        }
    }

    private fun observeFilteredReports(zonedDateTime: ZonedDateTime) {
        filteredReportsJob = viewModelScope.launch {
            if (::allReportsJob.isInitialized) {
                allReportsJob.cancelAndJoin()
            }
//            MongoDB.getFilteredNotes(zonedDateTime = zonedDateTime).collect { result ->
//                reports.value = result
//            }
        }
    }

}
