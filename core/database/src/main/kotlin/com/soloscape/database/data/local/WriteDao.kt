package com.soloscape.database.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soloscape.database.domain.model.Write
import kotlinx.coroutines.flow.Flow

@Dao
interface WriteDao {

    @Query("SELECT * FROM write ORDER BY date DESC")
    fun getWrites(): Flow<List<Write>>

    @Query("SELECT * FROM write WHERE id = :id")
    suspend fun getWriteById(id: Int): Write?

    @Query("SELECT * FROM write WHERE date BETWEEN :startEpoch AND :endEpoch")
    fun getWritesFiltered(startEpoch: Long, endEpoch: Long): Flow<List<Write>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWrite(write: Write)

    @Delete
    suspend fun deleteWrite(write: Write)

    @Query("DELETE FROM write")
    suspend fun deleteAllWrites()
}
