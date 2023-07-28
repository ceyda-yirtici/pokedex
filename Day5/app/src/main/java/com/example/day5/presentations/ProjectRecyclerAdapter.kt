package com.example.day5.presentations

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.day5.R
import com.example.day5.model.PokeGroup
import com.example.day5.model.CharacterDetail
import com.example.day5.model.DetailCharacterSprites
import com.example.day5.model.OfficialArtwork
import com.example.day5.model.OfficialArtworkSprites

class ProjectRecyclerAdapter : RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder>() {

    private var group: PokeGroup = PokeGroup(arrayListOf())
    private var detail: ArrayList<CharacterDetail> = arrayListOf()


    fun updateList(item: List<CharacterDetail>) {
        detail.clear()
        item.let {
            detail.addAll(it)
        }
        notifyDataSetChanged()
    }

    inner class ProjectViewHolder(itemView: View) : ViewHolder(itemView) {

        val character: TextView = itemView.findViewById(R.id.character)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val groupName: TextView = itemView.findViewById(R.id.group_name)


        fun bind(detail: CharacterDetail) {
            Log.i("projectviewholder", detail.sprites.other.official_artwork.front_default)
            Glide.with(photo).load(detail.sprites.other.official_artwork.front_default).into(photo)
            character.text = detail.name ?: ""



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_project, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun getItemCount() = detail.size

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
       holder.bind(detail[position])

    }


}