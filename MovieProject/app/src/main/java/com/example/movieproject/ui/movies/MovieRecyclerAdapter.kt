package com.example.movieproject.ui.movies

import android.content.Context
import android.content.res.Resources
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
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.model.MovieList
import com.example.movieproject.room.AppDatabase
import com.example.movieproject.room.Movie
import com.example.movieproject.utils.BundleKeys


class MovieRecyclerAdapter() : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    private var movieList: MovieList = MovieList(arrayListOf(),0,0)
    private var genreMapper : HashMap<Int, String> = HashMap()
    private var likedMovieIds: List<Int> = emptyList()
     var currentPage: Int = 1
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var  onBottomReachedListener: OnBottomReachedListener

    private lateinit var listener: OnClickListener

    interface OnClickListener {
        fun onMovieClick(position: Int,  movieList: View, pokeList: ArrayList<MovieDetail> )
        fun onHeartButtonClick(
            adapterPosition: Int,
            movieList: View,
            results: ArrayList<MovieDetail>
        )


    }
    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
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
    fun setLikedMovieIds(movies: List<Int>) {
        likedMovieIds = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View,  listener: OnClickListener) : ViewHolder(itemView) {


        val movie: TextView = itemView.findViewById(R.id.movie)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val movie_desc: TextView = itemView.findViewById(R.id.movie_description)
        val heartButton: ImageButton = itemView.findViewById(R.id.heart_in_list)
        val date: TextView = itemView.findViewById(R.id.release_date)

        init {
            itemView.setOnClickListener {
                listener.onMovieClick(adapterPosition,itemView, movieList.results)
            }
            heartButton.setOnClickListener {
                val position = adapterPosition
                listener.onHeartButtonClick(position, itemView, movieList.results)
            }
        }
        fun bind(detail: MovieDetail) {
            Glide.with(photo).load(BundleKeys.baseImageUrl + detail.poster_path).into(photo)
            movie.text = detail.title
            movie_desc.text = detail.overview
            date.text = detail.release_date.subSequence(0,4)

            if (likedMovieIds.contains(detail.id)) {
                // Movie is liked, update the UI accordingly
                heartButton.setImageResource(R.drawable.heart_shape_red)
                heartButton.tag = "filled"
            } else {
                // Movie is not liked, update the UI accordingly
                heartButton.setImageResource(R.drawable.heart_shape_outlined)
                heartButton.tag = "outline"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView, listener)
    }

    override fun getItemCount() = movieList.results.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList.results[position])
        if (position == movieList.results.size - 1) {
            onBottomReachedListener.onBottomReached(position)
            currentPage += 1
        }
        if(!genreMapper.isEmpty()) {
            decideAddingGenreView(holder, position)
        }

    }
    fun sendGenreList(it: HashMap<Int, String>) {
        handler.post {
            genreMapper = it
            notifyDataSetChanged()
        }
    }
    private fun convertPixelsToDp(px: Int, context: Context): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }
    private fun decideAddingGenreView(holder: MovieViewHolder, position: Int){

        val genreContainer: LinearLayout = holder.itemView.findViewById(R.id.genreContainer)
        // Clear any previous genres before adding new ones
        genreContainer.removeAllViews()

        // Get the width of the views
        val photoImageView: ImageView = holder.itemView.findViewById(R.id.photo)
        val photoWidth = photoImageView.layoutParams.width
        val screenWidthInDp = convertPixelsToDp(Resources.getSystem().displayMetrics.widthPixels, holder.itemView.context)
        val availableWidthInDp = screenWidthInDp - convertPixelsToDp(photoWidth, holder.itemView.context)

        // Add each genre to the LinearLayout as separate rounded corner boxes
        var totalWidthInDp = 0
        for (genreId in movieList.results[position].genre_ids) {
            val genreName = genreMapper[genreId]
            Log.d("GenreAdapter", "Genre Name: $genreName")
            val genreView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_genre, genreContainer, false)
            val genreTextView = genreView.findViewById<TextView>(R.id.genreTextView)
            genreTextView.text = genreName

            // Measure the genreView width and add it to the totalWidth
            genreView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            totalWidthInDp += convertPixelsToDp(genreView.measuredWidth, holder.itemView.context)
            totalWidthInDp += convertPixelsToDp(photoImageView.marginEnd +
                    photoImageView.marginBottom +
                    genreContainer.marginEnd , holder.itemView.context)
            // Check if the totalWidth exceeds the availableWidth
            if (totalWidthInDp > availableWidthInDp) {
                break
            }
            // Add the genre item to the actual genreContainer
            genreContainer.addView(genreView)
        }
    }


}