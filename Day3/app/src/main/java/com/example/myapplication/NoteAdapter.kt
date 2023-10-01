package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    val notes: ArrayList<RecyclerItem>,
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var nListener: OnNoteClickListener
    interface OnNoteClickListener {
        fun onNoteClick(position: Int)
        fun onDeleteButtonClick(position: Int)
        fun onEditButtonClick(position: Int)

    }
    fun setOnClickListener(listener: OnNoteClickListener){
        nListener = listener
    }


     inner class ViewHolder(itemView: View, listener: OnNoteClickListener) : RecyclerView.ViewHolder(itemView){
         val tvNoteTitle: TextView = itemView.findViewById(R.id.titleTextView)
         val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
         val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
         val editButton: ImageButton = itemView.findViewById(R.id.editButton)

         init {

             itemView.setOnClickListener {
                 val position = adapterPosition
                 listener.onNoteClick(position)
             }
             deleteButton.setOnClickListener {
                 val position = adapterPosition
                 listener.onDeleteButtonClick(position)
             }
             editButton.setOnClickListener {
                 val position = adapterPosition
                 listener.onEditButtonClick(position)
             }
         }
         fun bind(note: RecyclerItem) {
             tvNoteTitle.text = note.title
             categoryTextView.text = note.tag
         }
     }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_bar, viewGroup, false)

        return ViewHolder(view, nListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(notes[position])



    }
    fun deleteNote(position: Int) {
        if (position in 0 until notes.size) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notes.size
    fun updateAt(position: Int, updatedNote: RecyclerItem) {
        if (position in 0 until notes.size) {
            notes[position] = updatedNote
            notifyItemChanged(position)
        }
    }

}