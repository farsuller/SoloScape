package com.soloscape.database.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soloscape.database.domain.model.Write


@Database(
    entities = [Write::class],
    version = 1
)
abstract class ScapeDatabase : RoomDatabase() {
    abstract val writeDao: WriteDao

    companion object {
        const val DATABASE_NAME = "scape_db"
    }

}