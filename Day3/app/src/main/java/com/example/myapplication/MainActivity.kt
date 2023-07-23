package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.DialogAddNoteBinding
import com.example.myapplication.room.AppDatabase
import com.example.myapplication.room.AppDatabaseProvider
import com.example.myapplication.room.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteClickListener {

    lateinit var recyclerView: RecyclerView

    lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter

    val db: AppDatabase by lazy {
        AppDatabaseProvider.getAppDatabase(this)
    }

    private var notesList: ArrayList<RecyclerItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            recycler.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }

        loadNotes()
        val addNoteButton: Button = binding.addNoteButton
        addNoteButton.setOnClickListener {
            showAddNoteDialog()

        }

    }
    override fun onNoteClick(position: Int, notes: List<RecyclerItem>) {
        // Handle item click here
        val clickedNote = notes[position]
        val detail = clickedNote.content


        // Navigate to a new page (e.g., NoteDetailsActivity) and pass the data
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
    override fun onEditButtonClick(position: Int, notes: List<RecyclerItem>) {
        val selectedNote = notesList[position]
        showEditNoteDialog(selectedNote, position)


    }


    override fun showEditNoteDialog(note: RecyclerItem, position: Int) {
        val binding: DialogAddNoteBinding = DialogAddNoteBinding.inflate(layoutInflater)
        binding.titleEditText.setText(note.name)
        binding.categoryEditText.setText(note.tag)
        binding.contentEditText.setText(note.content)


        val builder = AlertDialog.Builder(this)
        builder.setView(binding.root)
            .setTitle("Edit Note")
            .setPositiveButton("Save") { dialog, _ ->
                val updatedTitle = binding.titleEditText.text.toString()
                val updatedCategory = binding.categoryEditText.text.toString()
                val updatedContent = binding.contentEditText.text.toString()

                val updatedNote = RecyclerItem(updatedTitle, updatedCategory, updatedContent)
                lifecycleScope.launch {
                    notesList[position] = updatedNote
                    noteAdapter.notifyItemChanged(position)
                    saveNotes()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



     override fun showAddNoteDialog() {
         val binding: DialogAddNoteBinding = DialogAddNoteBinding.inflate(layoutInflater)

         val builder = AlertDialog.Builder(this)
         builder.setView(binding.root)
             .setTitle("Add New Note")
             .setPositiveButton("Add") { dialog, _ ->
                 val updatedTitle = binding.titleEditText.text.toString()
                 val updatedCategory = binding.categoryEditText.text.toString()
                 val updatedContent = binding.contentEditText.text.toString()

                 addNote(updatedTitle, updatedCategory, updatedContent)

                 dialog.dismiss()
             }
             .setNegativeButton("Cancel") { dialog, _ ->
                 dialog.dismiss()
             }
             .show()
    }
    private fun saveNotes() {
        lifecycleScope.launch {
            try {
                // Execute the database operation on the IO dispatcher
                withContext(Dispatchers.IO) {
                    // Clear the existing notes in the database
                    db.noteDao().deleteAllNotes()

                    // Map and insert all notes from the notesList into the database
                    val notesEntities = notesList.map { recyclerItem ->
                        Note(
                            title = recyclerItem.name ?: "",
                            category = recyclerItem.tag ?: "",
                            content = recyclerItem.content ?: ""
                        )
                    }
                    db.noteDao().insertAll(notesEntities)
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur during database operations
                // (e.g., SQLiteConstraintException, etc.)
                Log.e("SaveNotes", "Error saving notes: ${e.message}")
            }
        }
    }




    override fun addNote(name: String, tag: String, content: String) {
        val newNote = RecyclerItem(name, tag, content)
        lifecycleScope.launch {
            // Execute the database operation on the IO dispatcher
            withContext(Dispatchers.IO) {
                // Insert the new note into the database
                db.noteDao().getAll().map { newNote}
            }

            // Update the local notesList and the RecyclerView
            notesList.add(newNote)
            noteAdapter.notifyItemInserted(notesList.size - 1)
        }
    }

    private fun loadNotes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val notes = db.noteDao().getAll()
            val list =
                notes.map { RecyclerItem(it.title ?: "", it.category ?: "", it.content ?: "") }
            binding.recycler.adapter = NoteAdapter(list as ArrayList<RecyclerItem>) { item ->
                val intent = Intent(this@MainActivity, NoteDetailsActivity::class.java)
                intent.putExtra("item", item)
                startActivity(intent)
            }
        }
    }


}

