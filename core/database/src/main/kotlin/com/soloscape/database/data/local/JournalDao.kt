package com.soloscape.database.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soloscape.database.domain.model.Journal
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Query("SELECT * FROM journal ORDER BY date DESC")
    fun getJournals(): Flow<List<Journal>>

    @Query("SELECT * FROM journal WHERE id = :id")
    suspend fun getJournalById(id: Int): Journal?

    @Query("SELECT * FROM journal WHERE date BETWEEN :startEpoch AND :endEpoch")
    fun getWritesFiltered(startEpoch: Long, endEpoch: Long): Flow<List<Journal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addJournal(write: Journal)

    @Delete
    suspend fun deleteJournal(write: Journal)

    @Query("DELETE FROM journal")
    suspend fun deleteAllJournals()
}
