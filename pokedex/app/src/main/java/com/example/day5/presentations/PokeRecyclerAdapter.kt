package com.example.day5.presentations

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.day5.R
import com.example.day5.model.CharacterDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PokeRecyclerAdapter( val onPokeClickListener: OnClickListener) : RecyclerView.Adapter<PokeRecyclerAdapter.PokeViewHolder>() {

    private var pokeList: ArrayList<CharacterDetail> = arrayListOf()
    private val handler = Handler(Looper.getMainLooper())

    interface OnClickListener {
        fun onPokeClick(position: Int, pokeList: ArrayList<CharacterDetail> )




    }

    fun updateList(item: List<CharacterDetail>) {
        handler.post {
            pokeList.clear()
            item.let {
                pokeList.addAll(it)
            }
            notifyDataSetChanged()
        }
    }


    inner class PokeViewHolder(itemView: View) : ViewHolder(itemView) {

        val character: TextView = itemView.findViewById(R.id.character)
        val photo: ImageView = itemView.findViewById(R.id.photo)

        init {
            itemView.setOnClickListener {
                onPokeClickListener.onPokeClick(adapterPosition, pokeList)
            }
        }
        fun bind(detail: CharacterDetail) {
            Glide.with(photo).load(detail.sprites.other.official_artwork.front_default).into(photo)
            character.text = detail.name ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_project, parent, false)
        return PokeViewHolder(itemView)
    }

    override fun getItemCount() = pokeList.size

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
       holder.bind(pokeList[position])

    }


}