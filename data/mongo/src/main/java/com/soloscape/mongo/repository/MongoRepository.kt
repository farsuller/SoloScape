package com.soloscape.mongo.repository

import com.soloscape.util.model.Report
import com.soloscape.util.model.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Reports = RequestState<Map<LocalDate, List<Report>>>

internal interface MongoRepository {

    fun configureTheRealm()
    fun getAllNotes(): Flow <Reports>
    fun getFilteredNotes(zonedDateTime: ZonedDateTime) : Flow<Reports>
    fun getSelectedNotes (reportId : ObjectId) :  Flow<RequestState<Report>>
   suspend fun addNewNotes(report: Report) : RequestState<Report>
   suspend fun updateNotes(report: Report) : RequestState<Report>
   suspend fun deleteNotes(id: ObjectId) : RequestState<Boolean>
   suspend fun deleteAllNotes () : RequestState<Boolean>
}