package com.rastete.noteappcompose.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rastete.noteappcompose.feature_note.data.data_source.dao.NoteDao
import com.rastete.noteappcompose.feature_note.data.data_source.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract val dao: NoteDao

    companion object {
        const val DATABASE_NAME = "noteapp.db"
    }
}