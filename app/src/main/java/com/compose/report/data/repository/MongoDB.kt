package com.compose.report.data.repository

import com.compose.report.model.Report
import com.compose.report.util.Constants.APP_ID
import com.compose.report.util.RequestState
import com.compose.report.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB : MongoRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm


    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Report::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Report>("ownerId == $0", user.id),
                        name = "User's Reports"
                    )
                }
                .log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)
        }
    }

    override fun getAllReports(): Flow<Reports> {
        return if (user != null) {
            try {
                realm.query<Report>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedReport(reportId: ObjectId): Flow<RequestState<Report>> {
        return if (user != null) {
            try {
                realm.query<Report>(query = "_id == $0", reportId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }

            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }

        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend fun addNewReport(report: Report): RequestState<Report> {
        return if (user != null) {
            realm.write {
                try {
                    val addedReport = copyToRealm(report.apply { ownerId = user.id })
                    RequestState.Success(data = addedReport)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }

        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateReport(report: Report): RequestState<Report> {
        return if (user != null) {
            realm.write {
                val queriedReport = query<Report>(query = "_id == $0", report._id).first().find()
                if (queriedReport != null) {
                    queriedReport.title = report.title
                    queriedReport.description = report.description
                    queriedReport.mood = report.mood
                    queriedReport.images = report.images
                    queriedReport.date = report.date
                    RequestState.Success(data = queriedReport)
                } else {
                    RequestState.Error(error = Exception("Queried Report does not exist"))
                }
            }

        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteReport(id: ObjectId): RequestState<Report> {
        return if (user != null) {
            realm.write {
                try {
                    val report = query<Report>(query = "_id == $0 AND ownerId == $1", id, user.id).find().first()
                    delete(report)
                    RequestState.Success(data = report)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }
}

private class UserNotAuthenticatedException : Exception("User is not Logged In.")