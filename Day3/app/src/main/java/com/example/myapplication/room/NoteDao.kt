package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.RecyclerItem

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes_table")
    fun getAll(): List<com.example.myapplication.room.Note>

    @Query("SELECT * FROM notes_table WHERE id IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<com.example.myapplication.room.Note>

    @Query("SELECT * FROM notes_table WHERE id LIKE :id")
    fun findByName(id: Long): com.example.myapplication.room.Note

    @Insert
    fun insertAll( notes: List<com.example.myapplication.room.Note>)

    @Delete
    fun delete(note: com.example.myapplication.room.Note)

    @Query("DELETE FROM notes_table")
     fun deleteAllNotes()

    @Update
    abstract fun update(updatedNote: Note)

}