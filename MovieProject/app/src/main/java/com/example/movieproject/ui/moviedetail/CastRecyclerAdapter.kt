package com.example.movieproject.ui.moviedetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.movieproject.R
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.movieproject.utils.BundleKeys

class CastRecyclerAdapter:  RecyclerView.Adapter<CastRecyclerAdapter.ViewHolder>() {
    private var itemList: MutableList<CastPerson> = mutableListOf()
    private lateinit var listener: CastRecyclerAdapter.OnClickListener

    interface OnClickListener {
        fun onCastClick(position: Int,  itemView: View, itemList: MutableList<CastPerson> )
    }
    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
    }

    fun updateList(item: MutableList<CastPerson>) {
        item.let {
            itemList = item
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {


            itemView.setOnClickListener {
                listener.onCastClick(adapterPosition,  itemView, itemList )
            }

        }

        @SuppressLint("SuspiciousIndentation")
        fun bind(person: CastPerson) {

            val name: TextView = itemView.findViewById(R.id.name)
            val character: TextView = itemView.findViewById(R.id.character)
            val photo: ImageView = itemView.findViewById(R.id.photo)

                Glide.with(photo).load(BundleKeys.baseImageUrl + person.photo_path)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(30)))
                    .placeholder(R.drawable.baseline_photo_24) // drawable as a placeholder
                    .error(R.drawable.baseline_photo_24) //  drawable if an error occurs
                    .into(photo)

            name.text = person.name
            character.text = person.character

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_cast, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        itemList[position].let { holder.bind(it) }
    }

}

