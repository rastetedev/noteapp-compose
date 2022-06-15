package com.rastete.noteappcompose.feature_note.data.data_source.dao

import androidx.room.*
import com.rastete.noteappcompose.feature_note.data.data_source.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteEntity")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}