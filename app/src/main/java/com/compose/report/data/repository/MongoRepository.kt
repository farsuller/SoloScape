package com.compose.report.data.repository

import com.compose.report.model.Report
import com.compose.report.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Reports = RequestState<Map<LocalDate, List<Report>>>

interface MongoRepository {

    fun configureTheRealm()
    fun getAllReports(): Flow <Reports>
    fun getSelectedReport (reportId : ObjectId) :  Flow<RequestState<Report>>
   suspend fun addNewReport(report: Report) : RequestState<Report>
   suspend fun updateReport(report: Report) : RequestState<Report>
   suspend fun deleteReport(id: ObjectId) : RequestState<Report>
}