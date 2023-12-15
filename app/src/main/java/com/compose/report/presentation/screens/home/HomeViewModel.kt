package com.compose.report.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.report.connectivity.ConnectivityObserver
import com.compose.report.connectivity.NetworkConnectivityObserver
import com.compose.report.data.database.ImageToDeleteDao
import com.compose.report.data.database.entity.ImageToDelete
import com.compose.report.data.repository.MongoDB
import com.compose.report.data.repository.Reports
import com.compose.report.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val imageToDeleteDao : ImageToDeleteDao
): ViewModel() {

    private lateinit var allReportsJob : Job
    private lateinit var filteredReportsJob : Job
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var reports : MutableState<Reports> = mutableStateOf(RequestState.Idle)
    var dateIsSelected by mutableStateOf(false)
        private set
    init {
        getReports()
        viewModelScope.launch {
            connectivity.observe().collect{ network = it }
        }
    }

    fun getReports(zonedDateTime: ZonedDateTime? = null){
        dateIsSelected = zonedDateTime != null
        reports.value = RequestState.Loading
        if (dateIsSelected && zonedDateTime != null){
            observeFilteredReports(zonedDateTime = zonedDateTime)
        }
        else{
            observeAllReports()
        }
    }
    private fun observeAllReports(){
        allReportsJob = viewModelScope.launch {
            if(::filteredReportsJob.isInitialized){
                filteredReportsJob.cancelAndJoin()
            }
            MongoDB.getAllReports().collect{ result ->
                reports.value = result
            }
        }
    }
    private fun observeFilteredReports(zonedDateTime: ZonedDateTime){
        filteredReportsJob = viewModelScope.launch {
            if(::allReportsJob.isInitialized){
                allReportsJob.cancelAndJoin()
            }
            MongoDB.getFilteredReports(zonedDateTime = zonedDateTime).collect{ result ->
                reports.value = result
            }
        }
    }

    fun deleteAllReports(
        onSuccess : () -> Unit,
        onError: (Throwable) -> Unit
    ){
        if (network == ConnectivityObserver.Status.Available){
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val imagesDirectory = "images/${userId}"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imagesDirectory)
                .listAll()
                .addOnSuccessListener {
                    it.items.forEach { ref->
                        val imagePath = "images/${userId}/${ref.name}"
                        storage.child(imagePath).delete()
                            .addOnFailureListener {
                                viewModelScope.launch(Dispatchers.IO){
                                    imageToDeleteDao.addImageToDelete(
                                        ImageToDelete(
                                            remoteImagePath = imagePath
                                        )
                                    )
                                }
                            }
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = MongoDB.deleteAllReports()
                        if(result is RequestState.Success){
                            withContext(Dispatchers.Main){
                                onSuccess()
                            }
                        }
                        else if (result is RequestState.Error){
                            withContext(Dispatchers.Main){
                                onError(result.error)
                            }
                        }
                    }
                }.addOnFailureListener { onError(it) }
        }else{
            onError(Exception("No Internet Connection."))
        }
    }
}