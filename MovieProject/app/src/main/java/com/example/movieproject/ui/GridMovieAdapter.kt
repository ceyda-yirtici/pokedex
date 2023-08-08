import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.movieproject.utils.BundleKeys

class GridMovieAdapter(private val context: Context) : BaseAdapter() {

    private var movieList: ArrayList<MovieDetail> = arrayListOf()
    private var likedMovieIds: List<Int> = emptyList()

    private lateinit var onBottomReachedListener: OnBottomReachedListener
    private lateinit var listener: OnClickListener

    interface OnClickListener {
        fun onMovieClick(position: Int,  itemView: View, movieList: ArrayList<MovieDetail> )
        fun onHeartButtonClick(
            adapterPosition: Int,
            itemView: View,
            movieList: ArrayList<MovieDetail>,
            heartButton: ImageButton
        )


    }
    interface OnBottomReachedListener {
        fun onBottomReached(position: Int)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }
    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
    }

    fun updateMovieList(newMovieList: ArrayList<MovieDetail>) {
        movieList.addAll(newMovieList)
        notifyDataSetChanged()
    }
    fun setLikedMovieIds(movies: List<Int>) {
        likedMovieIds = movies
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return movieList.size
    }

    override fun getItem(position: Int): Any {
        return movieList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val movie = getItem(position) as MovieDetail
        val viewHolder: ViewHolder

        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_movie_grid, parent, false)
            viewHolder = ViewHolder(view, position, listener)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.titleTextView.text = movie.title
        Glide.with(context)
            .load(BundleKeys.baseImageUrl + movie.poster_path)
            .into(viewHolder.posterImageView)

        if (likedMovieIds.contains(movie.id)) {
            movie.heart_tag = "filled"
        }
        if ( movie.heart_tag == "filled") {
            // Movie is liked, update the UI accordingly
            viewHolder.heartButton.setImageResource(R.drawable.heart_shape_red)
            viewHolder.heartButton.tag = "filled"
        } else {
            // Movie is not liked, update the UI accordingly
            viewHolder.heartButton.setImageResource( R.drawable.heart_shape_grey)
            viewHolder.heartButton.tag = "outline"
        }

        // Check if the last item is being displayed and trigger the bottom reached callback
        if (position == movieList.size - 1) {
            onBottomReachedListener.onBottomReached(position)
        }

        return view
    }


    inner class ViewHolder(view: View, position:Int, listener: OnClickListener) {
        val posterImageView: ImageView = view.findViewById(R.id.photoGrid)
        val titleTextView: TextView = view.findViewById(R.id.title_grid)
        val heartButton: ImageButton = view.findViewById(R.id.heart_in_grid)

        init {
            view.setOnClickListener {
                listener.onMovieClick(position,view, movieList)
            }
            heartButton.setOnClickListener {
                listener.onHeartButtonClick(position, view, movieList, heartButton)
            }

        }


    }
}
