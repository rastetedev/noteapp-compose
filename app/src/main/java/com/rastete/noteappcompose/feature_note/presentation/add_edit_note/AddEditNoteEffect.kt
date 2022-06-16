package com.rastete.noteappcompose.feature_note.presentation.add_edit_note

sealed class AddEditNoteEffect {
    object SaveNote : AddEditNoteEffect()
    data class ShowSnackBarError(val message: String) : AddEditNoteEffect()
}