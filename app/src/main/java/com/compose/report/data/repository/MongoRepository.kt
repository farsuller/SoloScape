package com.compose.report.data.repository

import com.compose.report.model.Report
import com.compose.report.model.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Reports = RequestState<Map<LocalDate, List<Report>>>

interface MongoRepository {

    fun configureTheRealm()
    fun getAllReports(): Flow <Reports>
    fun getFilteredReports(zonedDateTime: ZonedDateTime) : Flow<Reports>
    fun getSelectedReport (reportId : ObjectId) :  Flow<RequestState<Report>>
   suspend fun addNewReport(report: Report) : RequestState<Report>
   suspend fun updateReport(report: Report) : RequestState<Report>
   suspend fun deleteReport(id: ObjectId) : RequestState<Report>
   suspend fun deleteAllReports () : RequestState<Boolean>
}