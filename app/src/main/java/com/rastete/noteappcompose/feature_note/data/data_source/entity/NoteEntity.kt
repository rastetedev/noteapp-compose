package com.rastete.noteappcompose.feature_note.data.data_source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rastete.noteappcompose.feature_note.domain.model.Note
import com.rastete.noteappcompose.feature_note.domain.model.NoteColor

@Entity
data class NoteEntity(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: String
) {

    fun toNote() = Note(
        id = id ?: 0,
        title = title,
        content = content,
        timestamp = timestamp,
        color = NoteColor.valueOf(color)
    )

    companion object {
        fun fromNote(note: Note) = NoteEntity(
            title = note.title,
            content = note.content,
            timestamp = note.timestamp,
            color = note.color.toString()
        )
    }

}
