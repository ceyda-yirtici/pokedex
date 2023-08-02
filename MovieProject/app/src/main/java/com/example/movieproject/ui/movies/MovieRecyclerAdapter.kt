package com.example.movieproject.ui.movies

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieList
import com.example.movieproject.utils.BundleKeys


class MovieRecyclerAdapter(val onMovieClickListener: OnClickListener) : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    private var movieList: MovieList = MovieList(arrayListOf(),0,0)
     var currentPage: Int = 1
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var  onBottomReachedListener: OnBottomReachedListener

    interface OnClickListener {
        fun onMovieClick(position: Int, pokeList: ArrayList<MovieDetail> )

    }
    interface OnBottomReachedListener {
        fun onBottomReached(position: Int)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }
    fun updateList(item: MovieList) {
        handler.post {
            item.let {
                movieList.results.addAll(it.results)
                Log.e("updated list", movieList.results.toString())
            }
            notifyDataSetChanged()
        }
    }


    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {

        val movie: TextView = itemView.findViewById(R.id.movie)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val movie_desc: TextView = itemView.findViewById(R.id.movie_description)

        init {
            itemView.setOnClickListener {
                onMovieClickListener.onMovieClick(adapterPosition, movieList.results)
            }
        }
        fun bind(detail: MovieDetail) {
            Glide.with(photo).load(BundleKeys.baseImageUrl + detail.poster_path).into(photo)
            movie.text = detail.title
            movie_desc.text = detail.overview

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun getItemCount() = movieList.results.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
       holder.bind(movieList.results[position])
        if (position == movieList.results.size - 1){
            onBottomReachedListener.onBottomReached(position)
            currentPage+=1
        }

    }


}