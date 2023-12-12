package com.compose.report.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.report.data.repository.MongoDB
import com.compose.report.data.repository.Reports
import com.compose.report.util.RequestState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var reports : MutableState<Reports> = mutableStateOf(RequestState.Idle)

    init {
        observeAllReports()
    }

    private fun observeAllReports(){
        viewModelScope.launch {
            MongoDB.getAllReports().collect{ result ->
                reports.value = result
            }
        }
    }
}