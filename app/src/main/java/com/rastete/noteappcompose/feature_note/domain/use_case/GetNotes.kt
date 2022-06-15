package com.rastete.noteappcompose.feature_note.domain.use_case

import com.rastete.noteappcompose.feature_note.domain.model.Note
import com.rastete.noteappcompose.feature_note.domain.repository.NoteRepository
import com.rastete.noteappcompose.feature_note.domain.util.NoteOrder
import com.rastete.noteappcompose.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(private val repository: NoteRepository) {

    operator fun invoke(order: NoteOrder = NoteOrder.Date(OrderType.Descending)): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (order.orderType) {
                is OrderType.Ascending -> {
                    when (order) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when (order) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}