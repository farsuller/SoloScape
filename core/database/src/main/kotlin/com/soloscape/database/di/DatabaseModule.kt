package com.soloscape.database.di

import android.content.Context
import androidx.room.Room
import com.soloscape.database.data.local.ScapeDatabase
import com.soloscape.database.data.repository.JournalRepositoryImpl
import com.soloscape.database.data.repository.NoteRepositoryImpl
import com.soloscape.database.domain.repository.JournalRepository
import com.soloscape.database.domain.repository.NoteRepository
import com.soloscape.database.domain.usecase.JournalUseCases
import com.soloscape.database.domain.usecase.NotesUseCases
import com.soloscape.database.domain.usecase.journal.AddJournal
import com.soloscape.database.domain.usecase.journal.DeleteAllJournal
import com.soloscape.database.domain.usecase.journal.DeleteJournal
import com.soloscape.database.domain.usecase.journal.GetJournalByFiltered
import com.soloscape.database.domain.usecase.journal.GetJournalById
import com.soloscape.database.domain.usecase.journal.GetJournals
import com.soloscape.database.domain.usecase.note.AddNote
import com.soloscape.database.domain.usecase.note.DeleteNote
import com.soloscape.database.domain.usecase.note.GetNote
import com.soloscape.database.domain.usecase.note.GetNotes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideScapeDatabase(@ApplicationContext context: Context): ScapeDatabase =
        Room.databaseBuilder(
            context = context,
            klass = ScapeDatabase::class.java,
            name = ScapeDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideJournalRepository(db: ScapeDatabase): JournalRepository =
        JournalRepositoryImpl(db.journalDao)

    @Provides
    @Singleton
    fun provideJournalUseCases(repository: JournalRepository): JournalUseCases = JournalUseCases(
        getJournals = GetJournals(repository = repository),
        getJournalById = GetJournalById(repository = repository),
        getJournalByFiltered = GetJournalByFiltered(repository = repository),
        addJournal = AddJournal(repository = repository),
        deleteJournal = DeleteJournal(repository = repository),
        deleteAllJournal = DeleteAllJournal(repository = repository),
    )

    @Provides
    @Singleton
    fun provideNoteRepository(db: ScapeDatabase): NoteRepository = NoteRepositoryImpl(db.noteDao)

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases = NotesUseCases(
        getNotes = GetNotes(repository = repository),
        deleteNote = DeleteNote(repository = repository),
        addNote = AddNote(repository = repository),
        getNote = GetNote(repository = repository),
    )
}
