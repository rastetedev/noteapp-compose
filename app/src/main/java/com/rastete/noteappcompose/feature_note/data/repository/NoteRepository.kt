package com.rastete.noteappcompose.feature_note.data.repository

import com.rastete.noteappcompose.feature_note.data.data_source.dao.NoteDao
import com.rastete.noteappcompose.feature_note.data.data_source.entity.NoteEntity
import com.rastete.noteappcompose.feature_note.domain.model.Note
import com.rastete.noteappcompose.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.*

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes().map { list ->
            list.map { noteEntity -> noteEntity.toNote() }
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(NoteEntity.fromNote(note))
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(NoteEntity.fromNote(note))
    }
}