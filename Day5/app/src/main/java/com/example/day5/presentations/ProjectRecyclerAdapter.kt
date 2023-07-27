package com.example.day5.presentations

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.day5.R
import com.example.day5.model.PokeGroup
import com.example.day5.model.Character

class ProjectRecyclerAdapter : RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder>() {

    private val group: PokeGroup = PokeGroup(arrayListOf())
    private val characterList: ArrayList<Character> = arrayListOf()

    fun updateList(item: PokeGroup) {
        group.characters.clear()
        item?.let {
            group.characters.addAll(it.characters)

        }
        notifyDataSetChanged()
    }

    inner class ProjectViewHolder(itemView: View) : ViewHolder(itemView) {

        val character: TextView = itemView.findViewById(R.id.character)
        val groupName: TextView = itemView.findViewById(R.id.group_name)

        fun bind(item: Character) {
            Log.i("projectviewholder", "character.text")
            character.text = item.name ?: ""

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun getItemCount() = group.characters.size

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        group.characters[position]?.let { holder.bind(it) }
    }
}