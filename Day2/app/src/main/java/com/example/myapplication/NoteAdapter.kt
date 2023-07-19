package com.example.myapplication

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.RecyclerItem

class NoteAdapter(val notes: List<RecyclerItem>,  val onNoteClickListener: OnNoteClickListener) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    interface OnNoteClickListener {
        fun onNoteClick(position: Int, notes: List<RecyclerItem>)
    }


     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
         val tvNoteTitle: TextView = itemView.findViewById(R.id.textView)

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

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvNoteTitle.text = notes[position].name.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = notes.size

}