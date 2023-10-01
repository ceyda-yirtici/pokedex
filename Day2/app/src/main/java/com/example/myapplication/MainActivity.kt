package com.example.myapplication


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteClickListener {

    lateinit var recyclerView: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences

    private var notesList: ArrayList<RecyclerItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("NotesPref", Context.MODE_PRIVATE)
        loadNotes()
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NoteAdapter(notesList, this@MainActivity)


        val addNoteButton: Button = findViewById(R.id.addNoteButton)
        addNoteButton.setOnClickListener {
            showAddNoteDialog()

        }

    }
    override fun onNoteClick(position: Int, notes: List<RecyclerItem>) {
        // Handle item click here
        val clickedNote = notes[position]
        val detail = clickedNote.content


        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra("detail_note", detail)
        startActivity(intent)
    }
    override fun onDeleteButtonClick(position: Int,  notes: List<RecyclerItem>) {
        // Show a confirmation dialog to delete the note

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { dialog, _ ->
                val adapter = recyclerView.adapter as NoteAdapter
                adapter.deleteNote(position)
                saveNotes()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()

    }
    override fun onEditButtonClick(position: Int,  notes: List<RecyclerItem>) {
        val selectedNote = notesList[position]
        showEditNoteDialog(selectedNote, position)


    }


    override fun showEditNoteDialog(note: RecyclerItem, position: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val editDialog = inflater.inflate(R.layout.dialog_add_note, null)

        // Find views in the dialog
        val titleEditText = editDialog.findViewById<EditText>(R.id.titleEditText)
        val categoryEditText = editDialog.findViewById<EditText>(R.id.categoryEditText)
        val contentEditText = editDialog.findViewById<EditText>(R.id.contentEditText)

        // Set existing note data to the dialog views
        titleEditText.setText(note.name)
        categoryEditText.setText(note.tag)
        contentEditText.setText(note.content)

        // Set click listener for the save button in the dialog

        builder.setView(editDialog)
            .setTitle("Edit Note")
            .setPositiveButton("Save") { dialog, _ ->
                val updatedTitle = titleEditText.text.toString()
                val updatedCategory = categoryEditText.text.toString()
                val updatedContent = contentEditText.text.toString()

                // Update the note in the notes list
                val adapter = recyclerView.adapter as NoteAdapter
                adapter.updateAt(position, RecyclerItem(updatedTitle, updatedCategory, updatedContent))
                saveNotes()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()


    }
     override fun showAddNoteDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_note, null)

        val titleEditText = dialogView.findViewById<EditText>(R.id.titleEditText)
        val categoryEditText = dialogView.findViewById<EditText>(R.id.categoryEditText)
        val contentEditText = dialogView.findViewById<EditText>(R.id.contentEditText)

        builder.setView(dialogView)
            .setTitle("Add New Note")
            .setPositiveButton("Add") { dialog, _ ->
                val name = titleEditText.text.toString()
                val tag = categoryEditText.text.toString()
                val content = contentEditText.text.toString()
                addNote(name, tag, content)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun saveNotes() {
        val notesString = notesList.joinToString(";") { "${it.name},${it.tag},${it.content}" }
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }
    private fun loadNotes() {
        val notesString = sharedPreferences.getString("notes", "")
        notesList.clear()
        if (!notesString.isNullOrEmpty()) {
            val notesArray = notesString.split(";")
            for (noteData in notesArray) {
                val noteDataArray = noteData.split(",")
                if (noteDataArray.size == 3) {
                    val title = noteDataArray[0]
                    val category = noteDataArray[1]
                    val content = noteDataArray[2]
                    notesList.add(RecyclerItem(title, category, content))
                }
            }
        }
    }
    // Function to add a new note
    override fun addNote(name: String, tag: String, content: String) {
        val newNote = RecyclerItem(name, tag, content)
        notesList.add(newNote)
        saveNotes()
        recyclerView.adapter?.notifyDataSetChanged()
    }


}