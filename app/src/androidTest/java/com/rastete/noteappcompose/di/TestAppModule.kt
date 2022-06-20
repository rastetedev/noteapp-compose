package com.rastete.noteappcompose.di

import android.app.Application
import androidx.room.Room
import com.rastete.noteappcompose.feature_note.data.data_source.NoteDatabase
import com.rastete.noteappcompose.feature_note.data.repository.NoteRepositoryImpl
import com.rastete.noteappcompose.feature_note.domain.repository.NoteRepository
import com.rastete.noteappcompose.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            NoteDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(database: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(dao = database.dao)
    }

    @Provides
    @Singleton
    fun providesNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}