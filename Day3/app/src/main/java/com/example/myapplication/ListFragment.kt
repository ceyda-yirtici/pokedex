package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.DialogAddNoteBinding
import com.example.myapplication.databinding.FragmentListBinding
import com.example.myapplication.room.AppDatabase
import com.example.myapplication.room.AppDatabaseProvider
import com.example.myapplication.room.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListFragment :Fragment(R.layout.fragment_list){
    private lateinit var binding: FragmentListBinding
    private lateinit var db: AppDatabase

    private var notesList: ArrayList<RecyclerItem> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabaseProvider.getAppDatabase(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNotes()
    }
    private fun showEditNoteDialog(position: Int) {
        val binding: DialogAddNoteBinding = DialogAddNoteBinding.inflate(layoutInflater)
        binding.titleEditText.setText(notesList[position].title)
        binding.categoryEditText.setText(notesList[position].tag)
        binding.contentEditText.setText(notesList[position].content)


        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Edit Note")
            .setView(binding.root)
            .setPositiveButton("Save") { dialog, _ ->

                val updatedTitle = binding.titleEditText.text.toString()
                val updatedCategory = binding.categoryEditText.text.toString()
                val updatedContent = binding.contentEditText.text.toString()
                editNote(position,updatedTitle, updatedCategory, updatedContent)
                saveNotes()

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()

    }

    private fun showAddNoteDialog() {
        val binding: DialogAddNoteBinding = DialogAddNoteBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
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
                            title = recyclerItem.title ?: "",
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


    private fun addNote(title: String, tag: String, content: String) {
        val newNote = Note(title = title,category= tag,content = content)

        lifecycleScope.launch {
            try {
                // Execute the database operation on the IO dispatcher
                withContext(Dispatchers.IO) {
                    db.noteDao().insert(newNote)
                }

                // Update the local notesList and the RecyclerView
                notesList.add(RecyclerItem(title, tag, content))
                binding.recycler.adapter?.notifyItemInserted(notesList.size - 1)

            } catch (e: Exception) {
                // Handle any exceptions that occur during database operations
                Log.e("AddNote", "Error adding new note: ${e.message}")
            }
        }
    }
    private fun editNote(position: Int, title: String, tag: String, content: String){
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    db.noteDao().update(title,tag,content,position)
                }

                // Update the local notesList and the RecyclerView
                val updatedNote = RecyclerItem(title, tag, content)
                notesList[position] = updatedNote
                binding.recycler.adapter?.notifyItemChanged(position)
            } catch (e: Exception) {
                // Handle any exceptions that occur during database operations
                Log.e("EditNote", "Error adding new note: ${e.message}")
            }
        }
    }

    private fun loadNotes() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val notes = db.noteDao().getAll()
            val list = notes.map { RecyclerItem(it.title ?: "", it.category ?: "", it.content ?: "") }
            withContext(Dispatchers.Main) {
                val adapter = NoteAdapter(list as ArrayList<RecyclerItem>)
                notesList = list

                adapter.setOnClickListener(object : NoteAdapter.OnNoteClickListener {

                    override fun onNoteClick(position: Int) {
                        // Handle item click here
                        val clickedNote = notesList[position]
                        val detail = clickedNote.content
                        val tag = clickedNote.tag
                        val title = clickedNote.title
                        val bundle = Bundle().apply {
                            putString(NoteDetailsFragment.REQUEST_DETAIL, detail)
                            putString(NoteDetailsFragment.REQUEST_KEY_TAG, tag)
                            putString(NoteDetailsFragment.REQUEST_KEY_TITLE, title)

                        }

                        val destinationFragment = NoteDetailsFragment()
                        destinationFragment.arguments = bundle
                        findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle) // R.id.action_detail


                    }

                    override fun onDeleteButtonClick(position: Int) {
                        // Show a confirmation dialog to delete the note

                        val alertDialog = AlertDialog.Builder(requireContext())
                            .setTitle("Delete Note")
                            .setMessage("Are you sure you want to delete this note?")
                            .setPositiveButton("Delete") { dialog, _ ->
                                val adapter = binding.recycler.adapter as NoteAdapter
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

                    override fun onEditButtonClick(position: Int) {
                        if (!notesList.isEmpty()) {
                            showEditNoteDialog(position)
                        }
                    }

                }
                )
                binding.recycler.adapter = adapter

                binding.addNoteButton.setOnClickListener {
                    showAddNoteDialog()
                }

            }

        }

    }
}