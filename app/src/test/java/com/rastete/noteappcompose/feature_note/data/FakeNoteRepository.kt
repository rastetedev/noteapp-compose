package com.rastete.noteappcompose.feature_note.data

import com.rastete.noteappcompose.feature_note.domain.model.Note
import com.rastete.noteappcompose.feature_note.domain.model.NoteColor
import com.rastete.noteappcompose.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class FakeNoteRepository : NoteRepository {

    private val notes = mutableListOf<Note>()

    override fun getNotes(): Flow<List<Note>> {
        return flow { emit(notes) }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    fun populateFakeDatabase() {
        runBlocking {
            for (i in 1..20) {
                insertNote(
                    Note(
                        title = ('a'..'z').random().toString(),
                        content = ('a'..'z').random().toString(),
                        timestamp = (1..20).random().toLong(),
                        color = NoteColor.values().random()
                    )
                )
            }
        }
    }
}