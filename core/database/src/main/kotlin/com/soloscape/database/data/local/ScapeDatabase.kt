package com.soloscape.database.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.model.Write

@Database(
    entities = [Write::class, Note::class],
    version = 2,
    exportSchema = true,
)
abstract class ScapeDatabase : RoomDatabase() {
    abstract val writeDao: WriteDao
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "scape_db"
    }
}
