package com.compose.report.data.repository

import com.compose.report.model.Report
import com.compose.report.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Reports = RequestState<Map<LocalDate, List<Report>>>

interface MongoRepository {

    fun configureTheRealm()

    fun getAllReports(): Flow <Reports>
}