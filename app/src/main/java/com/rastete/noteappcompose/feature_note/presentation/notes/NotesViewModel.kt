package com.rastete.noteappcompose.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.noteappcompose.feature_note.domain.model.Note
import com.rastete.noteappcompose.feature_note.domain.use_case.NoteUseCases
import com.rastete.noteappcompose.feature_note.domain.util.NoteOrder
import com.rastete.noteappcompose.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        orderNotes(
            NotesEvents.Order(NoteOrder.Date(OrderType.Descending))
        )
    }

    fun onEvent(event: NotesEvents) {
        when (event) {
            is NotesEvents.Order -> {
                orderNotes(event)
            }
            is NotesEvents.DeleteNote -> {
                deleteNote(event)
            }
            is NotesEvents.RestoreNote -> {
                restoreNote()
            }
            is NotesEvents.ToggleOrderSection -> {
                toggleOrderSection()
            }
        }
    }

    private fun orderNotes(event: NotesEvents.Order) {
        if (state.value.noteOrder::class == event.noteOrder::class &&
            state.value.noteOrder.orderType == event.noteOrder.orderType
        ) {
            return
        }
        getNotes(event)
    }

    private fun getNotes(event: NotesEvents.Order) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(event.noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = event.noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    private fun toggleOrderSection() {
        _state.value = state.value.copy(
            isOrderSectionVisible = !state.value.isOrderSectionVisible
        )
    }

    private fun restoreNote() {
        viewModelScope.launch {
            noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
            recentlyDeletedNote = null
        }
    }

    private fun deleteNote(event: NotesEvents.DeleteNote) {
        viewModelScope.launch {
            noteUseCases.deleteNote(event.note)
            recentlyDeletedNote = event.note
        }
    }


}