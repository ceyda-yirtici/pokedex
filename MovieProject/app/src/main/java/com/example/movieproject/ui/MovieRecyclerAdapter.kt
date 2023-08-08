package com.example.movieproject.ui

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
import com.example.movieproject.utils.BundleKeys


class MovieRecyclerAdapter : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    private var movieList: ArrayList<MovieDetail> = arrayListOf()
    private var genreMapper : HashMap<Int, String> = HashMap()
    private var likedMovieIds: List<Int> = emptyList()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var onBottomReachedListener: OnBottomReachedListener
    private lateinit var listener: OnClickListener
    private var viewMovieType: Boolean = true

    interface OnClickListener {
        fun onMovieClick(position: Int,  movieView: View, movieList: ArrayList<MovieDetail> )
        fun onHeartButtonClick(
            adapterPosition: Int,
            movieView: View,
            movieList: ArrayList<MovieDetail>,
            heartButton: ImageButton
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
    fun updateList(item: ArrayList<MovieDetail>) {
        handler.post {
            item.forEach { movieDetail ->
                movieDetail.heart_tag = "filled"
            }
            movieList = item
            Log.d("upp", movieList.toString())
            notifyDataSetChanged()
        }
    }
    fun updateViewType(viewType: Boolean){
        viewMovieType = viewType
    }

    fun addToList(item: ArrayList<MovieDetail>) {
        handler.post {
            item.let {
                movieList.addAll(it)
            }
            Log.d("upp", movieList.toString())
            notifyDataSetChanged()
        }
    }

    fun setLikedMovieIds(movies: List<Int>) {
        likedMovieIds = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(itemView: View, listener: OnClickListener, viewMovieType: Boolean) : ViewHolder(itemView) {


        private var  heartButton: ImageButton

        init {

            if(viewMovieType) heartButton = itemView.findViewById(R.id.heart_in_detail)
            else heartButton = itemView.findViewById(R.id.heart_in_grid)

            itemView.setOnClickListener {
                listener.onMovieClick(adapterPosition,itemView, movieList)
            }
            heartButton.setOnClickListener {
                val position = adapterPosition
                listener.onHeartButtonClick(position, itemView, movieList, heartButton)
            }

        }
        fun bind(detail: MovieDetail) {
            if(viewMovieType) {
                val movie: TextView = itemView.findViewById(R.id.movie)
                val photo: ImageView = itemView.findViewById(R.id.photo)
                val movie_desc: TextView = itemView.findViewById(R.id.movie_description)

                val date: TextView = itemView.findViewById(R.id.release_date)

                Glide.with(photo).load(BundleKeys.baseImageUrl + detail.poster_path).into(photo)
                movie.text = detail.title
                movie_desc.text = detail.overview
                date.text = detail.release_date.subSequence(0, 4)
            }
            else {

                val movie: TextView = itemView.findViewById(R.id.title_grid)
                val photo: ImageView = itemView.findViewById(R.id.photoGrid)

                Glide.with(photo).load(BundleKeys.baseImageUrl + detail.poster_path).into(photo)
                movie.text = detail.title
            }


            if (likedMovieIds.contains(detail.id)) {
                detail.heart_tag = "filled"
            }
            if ( detail.heart_tag == "filled") {
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

        return if(viewMovieType) {
            val itemView = inflater.inflate(R.layout.item_movie, parent, false)
            MovieViewHolder(itemView, listener, viewMovieType)
        } else{
            val itemView = inflater.inflate(R.layout.item_movie_grid, parent, false)
            MovieViewHolder(itemView, listener, viewMovieType)
        }
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])

        if (position == movieList.size -1 ) {
            onBottomReachedListener.onBottomReached(position)
        }

        if (viewMovieType) {
            val genreNamesOfTheMovies = arrayListOf<String>()
            if (genreMapper != emptyMap<Int, String>()) {

                for (genreId in movieList[position].genre_ids) {

                    val genreName = genreMapper[genreId]
                    if (genreName != null) {
                        genreNamesOfTheMovies.add(genreName)
                    }
                }
            } else genreNamesOfTheMovies.addAll(movieList[position].genres.map { it.genre_name })


            decideAddingGenreView(holder, genreNamesOfTheMovies)
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
    private fun decideAddingGenreView(holder: MovieViewHolder, genre_names: ArrayList<String>){

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
        for (genreName in genre_names) {
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