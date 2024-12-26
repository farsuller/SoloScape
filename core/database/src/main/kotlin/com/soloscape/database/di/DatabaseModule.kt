package com.soloscape.database.di

import android.content.Context
import androidx.room.Room
import com.soloscape.database.data.local.ScapeDatabase
import com.soloscape.database.data.repository.NoteRepositoryImpl
import com.soloscape.database.data.repository.WriteRepositoryImpl
import com.soloscape.database.domain.repository.NoteRepository
import com.soloscape.database.domain.repository.WriteRepository
import com.soloscape.database.domain.usecase.NotesUseCases
import com.soloscape.database.domain.usecase.WriteUseCases
import com.soloscape.database.domain.usecase.note.AddNote
import com.soloscape.database.domain.usecase.note.DeleteNote
import com.soloscape.database.domain.usecase.note.GetNote
import com.soloscape.database.domain.usecase.note.GetNotes
import com.soloscape.database.domain.usecase.write.AddWrite
import com.soloscape.database.domain.usecase.write.DeleteAllWrite
import com.soloscape.database.domain.usecase.write.DeleteWrite
import com.soloscape.database.domain.usecase.write.GetWriteByFiltered
import com.soloscape.database.domain.usecase.write.GetWriteById
import com.soloscape.database.domain.usecase.write.GetWrites
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
    fun provideScapeDatabase(@ApplicationContext context: Context): ScapeDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ScapeDatabase::class.java,
            name = ScapeDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWriteRepository(db: ScapeDatabase): WriteRepository {
        return WriteRepositoryImpl(db.writeDao)
    }

    @Provides
    @Singleton
    fun provideWriteUseCases(repository: WriteRepository): WriteUseCases {
        return WriteUseCases(
            getWrite = GetWrites(repository = repository),
            getWriteById = GetWriteById(repository = repository),
            getWriteByFiltered = GetWriteByFiltered(repository = repository),
            addWrite = AddWrite(repository = repository),
            deleteWrite = DeleteWrite(repository = repository),
            deleteAllWrite = DeleteAllWrite(repository = repository),
        )
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: ScapeDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NotesUseCases {
        return NotesUseCases(
            getNotes = GetNotes(repository = repository),
            deleteNote = DeleteNote(repository = repository),
            addNote = AddNote(repository = repository),
            getNote = GetNote(repository = repository),
        )
    }
}