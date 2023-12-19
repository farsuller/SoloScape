package com.soloscape.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soloscape.util.connectivity.ConnectivityObserver
import com.soloscape.util.connectivity.NetworkConnectivityObserver
import com.soloscape.util.model.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.soloscape.mongo.database.ImageToDeleteDao
import com.soloscape.mongo.database.entity.ImageToDelete
import com.soloscape.mongo.repository.MongoDB
import com.soloscape.mongo.repository.Reports
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
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
    @OptIn(FlowPreview::class)
    private fun observeAllReports(){
        allReportsJob = viewModelScope.launch {
            if(::filteredReportsJob.isInitialized){
                filteredReportsJob.cancelAndJoin()
            }
            MongoDB.getAllNotes().debounce(2000).collect{ result ->
                reports.value = result
            }
        }
    }
    private fun observeFilteredReports(zonedDateTime: ZonedDateTime){
        filteredReportsJob = viewModelScope.launch {
            if(::allReportsJob.isInitialized){
                allReportsJob.cancelAndJoin()
            }
            MongoDB.getFilteredNotes(zonedDateTime = zonedDateTime).collect{ result ->
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
                        val result = MongoDB.deleteAllNotes()
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