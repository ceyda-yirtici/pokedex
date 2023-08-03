package com.example.movieproject.ui.movies

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.model.GenreList
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieGenre
import com.example.movieproject.model.MovieList
import com.example.movieproject.utils.BundleKeys


class MovieRecyclerAdapter() : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    private var movieList: MovieList = MovieList(arrayListOf(),0,0)
    private lateinit var genreMapper : HashMap<Int, String>
     var currentPage: Int = 1
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var  onBottomReachedListener: OnBottomReachedListener

    private lateinit var nListener: OnClickListener

    interface OnClickListener {
        fun onMovieClick(position: Int, pokeList: ArrayList<MovieDetail> )
        fun onHeartButtonClick(adapterPosition: Int, movieList: View)

    }
    fun setOnClickListener(listener: OnClickListener){
        nListener = listener
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


    inner class MovieViewHolder(itemView: View,  listener: OnClickListener) : ViewHolder(itemView) {

        val movie: TextView = itemView.findViewById(R.id.movie)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val movie_desc: TextView = itemView.findViewById(R.id.movie_description)
        val heartButton: ImageButton = itemView.findViewById(R.id.heart_in_list)
        val date: TextView = itemView.findViewById(R.id.release_date)

        init {
            itemView.setOnClickListener {
                listener.onMovieClick(adapterPosition, movieList.results)
            }
            heartButton.setOnClickListener {
                val position = adapterPosition
                listener.onHeartButtonClick(position, itemView)
            }
        }
        fun bind(detail: MovieDetail) {
            Glide.with(photo).load(BundleKeys.baseImageUrl + detail.poster_path).into(photo)
            movie.text = detail.title
            movie_desc.text = detail.overview
            date.text = detail.release_date.subSequence(0,4)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView, nListener)
    }

    override fun getItemCount() = movieList.results.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList.results[position])
        if (position == movieList.results.size - 1) {
            onBottomReachedListener.onBottomReached(position)
            currentPage += 1
        }

        val genreContainer: LinearLayout = holder.itemView.findViewById(R.id.genreContainer)

        // Clear any previous genres before adding new ones
        genreContainer.removeAllViews()
        // Add each genre to the LinearLayout as separate rounded corner boxes
        for (genreId in movieList.results[position].genre_ids) {
            val genreName = genreMapper[genreId]
            Log.d("GenreAdapter", "Genre Name: $genreName")

            val genreView =
                LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_genre, genreContainer, false)
            val genreTextView = genreView.findViewById<TextView>(R.id.genreTextView)
            genreTextView.text = genreName
            genreContainer.addView(genreView)
        }


    }
    fun sendGenreList(it: HashMap<Int, String>) {
        genreMapper = it
    }


}