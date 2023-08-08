package com.example.movieproject.ui.movies

import GridMovieAdapter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentMoviesBinding
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.Movie
import com.example.movieproject.ui.moviedetail.DetailMovieFragment
import com.example.movieproject.utils.BundleKeys
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies){

    private lateinit var movieDao: MovieDao
    private lateinit var binding: FragmentMoviesBinding
    private var pageCount = 1
    private val viewModel: MoviesViewModel by viewModels(ownerProducer = { this })
    private lateinit var movieRecyclerAdapter: MovieRecyclerAdapter
    private lateinit var gridMovieAdapter: GridMovieAdapter
    private lateinit var loadingView: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var gridView: GridView
    private var heartResource: Int = R.drawable.heart_shape_grey
    private var heartDrawable: Drawable = ColorDrawable(Color.TRANSPARENT)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = "Popular Movies"
        Log.d("view", "created")
        val database = AppDatabaseProvider.getAppDatabase(requireActivity().application)
        movieDao = database.movieDao()

        initView(view)
        listenGridView()
        //listenViewModel()
    }


    private fun initView(view: View){
        view.apply {

            //val layoutManager = LinearLayoutManager(requireContext())
            /*
            recyclerView = findViewById(R.id.recycler)
            recyclerView.layoutManager = layoutManager
            // Create the adapter instance
            movieRecyclerAdapter = MovieRecyclerAdapter()
            // Set the adapter to the RecyclerView
            recyclerView.adapter = movieRecyclerAdapter*/
            gridView = findViewById(R.id.gridView)
            gridMovieAdapter = GridMovieAdapter(requireContext())
            heartResource = R.drawable.heart_shape_grey
            // Set the adapter for the GridView
            gridView.adapter = gridMovieAdapter
            loadingView = findViewById(R.id.loading)
        }

    }


    private fun listenGridView() {
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner) {
                gridMovieAdapter.updateMovieList(it.results)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                gridView.visibility = if (it) View.GONE else View.VISIBLE
            }
            viewModel.liveDataLikedMovieIds.observe(viewLifecycleOwner) {
                gridMovieAdapter.setLikedMovieIds(it)
            }
            gridMovieAdapter.setOnClickListener(object : GridMovieAdapter.OnClickListener{
                override fun onMovieClick(position: Int, movieView : View,  movieList: ArrayList<MovieDetail>) {
                    movieClicked(position, movieView, movieList)
                }

                override fun onHeartButtonClick(
                    adapterPosition: Int,
                    movieView: View,
                    results: ArrayList<MovieDetail>,
                    heartButton: ImageButton
                ) {
                    heartButtonClicked(adapterPosition, movieView, results, heartButton)


                }

            })
            gridMovieAdapter.setOnBottomReachedListener(object : GridMovieAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    Log.e("initview", "you reached end")
                    pageCount++
                    viewModel.setPageNumber(pageCount)
                    viewModel.displayGroup(pageCount)


                }
            })
        }
    }
    private fun listenViewModel() {
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.addToList(it.results)
            }
            liveDataGenreList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.sendGenreList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                recyclerView.visibility = if (it) View.GONE else View.VISIBLE
            }
            viewModel.liveDataLikedMovieIds.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.setLikedMovieIds(it)
            }
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    Log.e("initview", "you reached end")
                    pageCount++
                    viewModel.setPageNumber(pageCount)
                    viewModel.displayGroup(pageCount)


                }
            })

            movieRecyclerAdapter.setOnClickListener(object : MovieRecyclerAdapter.OnClickListener{
                override fun onMovieClick(position: Int, movieView : View,  movieList: ArrayList<MovieDetail>) {
                    movieClicked(position, movieView, movieList)
                }

                override fun onHeartButtonClick(
                    adapterPosition: Int,
                    movieView: View,
                    results: ArrayList<MovieDetail>,
                    heartButton : ImageButton
                ) {
                    heartButtonClicked(adapterPosition, movieView, results, heartButton)


                }

            })

        }
    }






    private fun movieClicked(position: Int, movieView:View, movieList: ArrayList<MovieDetail>) {
        val clickedMovie = movieList[position]
        val id = clickedMovie.id
        val heart_tag = clickedMovie.heart_tag
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_ID, id)
            putInt(BundleKeys.position, position)
            putString(BundleKeys.HEART_TAG, heart_tag)
        }

        val destinationFragment = DetailMovieFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_detail, bundle) // R.id.action_detail

    }

    private fun heartButtonClicked(
        position: Int,
        itemView: View,
        results: ArrayList<MovieDetail>,
        heartButton: ImageButton
    ){

        val clickedMovie = results[position]

        if ( clickedMovie.heart_tag == "filled") {
            heartButton.setImageResource(heartResource)
            clickedMovie.heart_tag = "outline"
            removeMovieFromDB(clickedMovie, heartButton)
        } else {
            heartButton.setImageResource(R.drawable.heart_shape_red)
            clickedMovie.heart_tag = "filled"
            addMovieToDB(clickedMovie, heartButton)
        }


    }
    private fun addMovieToDB(clickedMovie: MovieDetail, heartButton: ImageButton ){

        val newMovie = Movie(movie_id = clickedMovie.id)
        lifecycleScope.launch {
            try {
                // Execute the database operation on the IO dispatcher
                withContext(Dispatchers.IO) {
                    movieDao.insert(newMovie)
                    viewModel.updateLikedMovieIds()
                }

            } catch (e: Exception) {
                heartButton.setImageResource(heartResource)
                clickedMovie.heart_tag = "outline"
            }
        }
    }
    private fun removeMovieFromDB(clickedMovie: MovieDetail, heartButton: ImageButton){

        lifecycleScope.launch {
            try {
                // Execute the database operation on the IO dispatcher
                withContext(Dispatchers.IO) {
                    movieDao.delete( viewModel.getMovieDao().get(clickedMovie.id))
                    viewModel.updateLikedMovieIds()
                }

            } catch (e: Exception) {
                heartButton.setImageResource(R.drawable.heart_shape_red)
                clickedMovie.heart_tag  = "filled"
            }
        }

    }



}