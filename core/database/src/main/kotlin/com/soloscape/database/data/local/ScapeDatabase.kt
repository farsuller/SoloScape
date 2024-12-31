package com.soloscape.database.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.model.Note

@Database(
    entities = [Journal::class, Note::class],
    version = 2,
    exportSchema = true,
)
abstract class ScapeDatabase : RoomDatabase() {
    abstract val journalDao: JournalDao
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "scape_db"
    }
}
