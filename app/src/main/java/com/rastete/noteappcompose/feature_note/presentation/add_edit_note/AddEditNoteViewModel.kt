package com.rastete.noteappcompose.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.noteappcompose.feature_note.domain.model.InvalidNoteException
import com.rastete.noteappcompose.feature_note.domain.model.Note
import com.rastete.noteappcompose.feature_note.domain.model.NoteColor
import com.rastete.noteappcompose.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitleState = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitleState: State<NoteTextFieldState> = _noteTitleState

    private val _noteContentState = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter some content..."
        )
    )
    val noteContentState: State<NoteTextFieldState> = _noteContentState

    private val _noteColorState = mutableStateOf(NoteColor.values().random())
    val noteColorState: State<NoteColor> = _noteColorState

    private val _addEditNoteEffect = MutableSharedFlow<AddEditNoteEffect>()
    val addEditNoteEffect: SharedFlow<AddEditNoteEffect> = _addEditNoteEffect.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>(NOTE_ID_ARGUMENT)?.let { noteId ->
            if (noteId != -1) {
                setupInitialState(noteId)
            }
        }
    }

    private fun setupInitialState(noteId: Int) {
        viewModelScope.launch {
            noteUseCases.getNote(noteId)?.also { note ->
                currentNoteId = note.id
                _noteTitleState.value = noteTitleState.value.copy(
                    text = note.title,
                    isHintVisible = false
                )
                _noteContentState.value = noteContentState.value.copy(
                    text = note.content,
                    isHintVisible = false
                )
                _noteColorState.value = note.color
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                updateTitle(event)
            }

            is AddEditNoteEvent.EnteredContent -> {
                updateContent(event)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                updateTitleFocus(event)
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                updateContentFocus(event)
            }

            is AddEditNoteEvent.ChangeColor -> {
                changeColor(event)
            }

            is AddEditNoteEvent.SaveNote -> {
                saveNote(event)
            }
        }
    }

    private fun saveNote(event: AddEditNoteEvent.SaveNote) {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(
                    Note(
                        title = noteTitleState.value.text,
                        content = noteContentState.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = noteColorState.value,
                        id = currentNoteId ?: 0
                    )
                )
                _addEditNoteEffect.emit(AddEditNoteEffect.SaveNote)
            } catch (e: InvalidNoteException) {
                _addEditNoteEffect.emit(
                    AddEditNoteEffect.ShowSnackBarError(
                        e.message ?: "Unknown error. Couldn't save note"
                    )
                )
            }
        }
    }

    private fun changeColor(event: AddEditNoteEvent.ChangeColor) {
        _noteColorState.value = event.color
    }

    private fun updateContentFocus(event: AddEditNoteEvent.ChangeContentFocus) {
        _noteContentState.value = noteContentState.value.copy(
            isHintVisible = !event.focusState.isFocused &&
                    noteContentState.value.text.isBlank()
        )
    }

    private fun updateTitleFocus(event: AddEditNoteEvent.ChangeTitleFocus) {
        _noteTitleState.value = noteTitleState.value.copy(
            isHintVisible = !event.focusState.isFocused &&
                    noteTitleState.value.text.isBlank()
        )
    }

    private fun updateTitle(event: AddEditNoteEvent.EnteredTitle) {
        _noteTitleState.value = noteTitleState.value.copy(
            text = event.value
        )
    }

    private fun updateContent(event: AddEditNoteEvent.EnteredContent) {
        _noteContentState.value = noteContentState.value.copy(
            text = event.value
        )
    }

    companion object {
        const val NOTE_ID_ARGUMENT = "noteId"
    }

}