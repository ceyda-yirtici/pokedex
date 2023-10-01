package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RecyclerItem

class NoteAdapter(val notes: ArrayList<RecyclerItem>,
                  val onNoteClickListener: OnNoteClickListener) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    interface OnNoteClickListener {
        fun onNoteClick(position: Int, notes: List<RecyclerItem>)
        fun onDeleteButtonClick(position: Int,  notes: List<RecyclerItem>)
        fun onEditButtonClick(position: Int,  notes: List<RecyclerItem>)
        fun showAddNoteDialog()
        fun addNote(name: String, tag: String, content: String)
        fun showEditNoteDialog(selectedNote: RecyclerItem, position: Int)

    }


     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
         val tvNoteTitle: TextView = itemView.findViewById(R.id.titleTextView)
         val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
         val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
         val editButton: ImageButton = itemView.findViewById(R.id.editButton)

         init {
             itemView.setOnClickListener(this)
         }

         override fun onClick(v: View) {
             onNoteClickListener.onNoteClick(adapterPosition, notes)
         }
     }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_bar, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvNoteTitle.text = notes[position].name
        viewHolder.categoryTextView.text = notes[position].tag

        viewHolder.deleteButton.setOnClickListener {
            // Call the onNoteClickListener's onNoteClick method
            onNoteClickListener.onDeleteButtonClick(position, notes)
        }
        viewHolder.editButton.setOnClickListener {
            // Call the onNoteClickListener's onNoteClick method
            onNoteClickListener.onEditButtonClick(position, notes)
        }
    }
    fun deleteNote(position: Int) {
        if (position in 0 until notes.size) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun updateAt(position: Int, updatedNote: RecyclerItem) {
        if (position in 0 until notes.size) {
            notes[position] = updatedNote
            notifyItemChanged(position)
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notes.size

}