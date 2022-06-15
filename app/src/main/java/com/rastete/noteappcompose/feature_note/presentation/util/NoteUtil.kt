package com.rastete.noteappcompose.feature_note.presentation.util

import androidx.compose.ui.graphics.Color
import com.rastete.noteappcompose.feature_note.domain.model.NoteColor
import com.rastete.noteappcompose.ui.theme.*

object NoteUtil {

    fun mapNoteColor(noteColor: NoteColor): Color {
        return when (noteColor) {
            NoteColor.BabyBlue -> BabyBlue
            NoteColor.LightGreen -> LightGreen
            NoteColor.RedOrange -> RedOrange
            NoteColor.RedPink -> RedPink
            NoteColor.Violet -> Violet
        }
    }
}