package com.soloscape.database.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.soloscape.database.data.local.ScapeDatabase
import com.soloscape.database.data.repository.WriteRepositoryImpl
import com.soloscape.database.domain.repository.WriteRepository
import com.soloscape.database.domain.usecase.AddWrite
import com.soloscape.database.domain.usecase.DeleteWrite
import com.soloscape.database.domain.usecase.GetWriteByFiltered
import com.soloscape.database.domain.usecase.GetWriteById
import com.soloscape.database.domain.usecase.GetWrites
import com.soloscape.database.domain.usecase.WriteUseCases
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
    fun provideScapeDatabase(@ApplicationContext context: Context,): ScapeDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ScapeDatabase::class.java,
            name = ScapeDatabase.DATABASE_NAME
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
            deleteWrite = DeleteWrite(repository = repository)
        )
    }
}