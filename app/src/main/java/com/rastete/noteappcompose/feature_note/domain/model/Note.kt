package com.rastete.noteappcompose.feature_note.domain.model

data class Note(
    val id: Int?,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: NoteColor
)

class InvalidNoteException(message:String) : Exception(message)