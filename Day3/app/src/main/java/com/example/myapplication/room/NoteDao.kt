package com.example.myapplication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes_table")
    fun getAll(): List<Note>

    @Query("SELECT * FROM notes_table WHERE id IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<com.example.myapplication.room.Note>

    @Insert
    fun insert( note: Note)

    @Insert
    fun insertAll( notes: List<com.example.myapplication.room.Note>)

    @Delete
    fun delete(note: com.example.myapplication.room.Note)

    @Query("DELETE FROM notes_table")
     fun deleteAllNotes()

    @Query("UPDATE notes_table SET  title= :title, category = :category, content = :content WHERE id = :position")
    fun update(title: String?, category: String?,content: String?, position: Int)




}