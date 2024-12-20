package com.soloscape.mongo.repository

import android.annotation.SuppressLint
import com.soloscape.util.Constants.APP_ID
import com.soloscape.util.model.Report
import com.soloscape.util.model.RequestState
import com.soloscape.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

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
                        name = "User's Reports",
                    )
                }
                .build()

            realm = Realm.open(config)
        }
    }

    @SuppressLint("NewApi")
    override fun getAllNotes(): Flow<Reports> {
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
                            },
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    @SuppressLint("NewApi")
    override fun getFilteredNotes(zonedDateTime: ZonedDateTime): Flow<Reports> {
        return if (user != null) {
            try {
                realm.query<Report>(
                    query = "ownerId == $0 AND date < $1 AND date > $2",
                    user.id,
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate().plusDays(1),
                            LocalTime.MIDNIGHT,
                        ).toEpochSecond(zonedDateTime.offset),
                        0,
                    ),
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate(),
                            LocalTime.MIDNIGHT,
                        ).toEpochSecond(zonedDateTime.offset),
                        0,
                    ),
                ).asFlow().map { result ->
                    RequestState.Success(
                        data = result.list.groupBy {
                            it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        },
                    )
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedNotes(reportId: ObjectId): Flow<RequestState<Report>> {
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

    override suspend fun addNewNotes(report: Report): RequestState<Report> {
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

    override suspend fun updateNotes(report: Report): RequestState<Report> {
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

    override suspend fun deleteNotes(id: ObjectId): RequestState<Boolean> {
        return if (user != null) {
            realm.write {
                val report = query<Report>(query = "_id == $0 AND ownerId == $1", id, user.id).first().find()
                if (report != null) {
                    try {
                        delete(report)
                        RequestState.Success(data = true)
                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(Exception("Report does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteAllNotes(): RequestState<Boolean> {
        return if (user != null) {
            realm.write {
                val reports = this.query<Report>("ownerId == $0", user.id).find()
                try {
                    delete(reports)
                    RequestState.Success(data = true)
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
