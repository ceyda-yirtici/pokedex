package com.example.movieproject.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
class MoviesFragment : Fragment(){

    private lateinit var movieDao: MovieDao
    private lateinit var binding: FragmentMoviesBinding
    private var pageCount = 1
    private val viewModel: MoviesViewModel by viewModels(ownerProducer = { this })
    private var movieRecyclerAdapter: MovieRecyclerAdapter = MovieRecyclerAdapter()
    private lateinit var loadingView: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private var heartResource: Int = R.drawable.heart_shape_grey
    private lateinit var viewButton: ImageButton
    private var listViewType = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("view", "create")
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = "Popular Movies"
        val database = AppDatabaseProvider.getAppDatabase(requireActivity().application)
        movieDao = database.movieDao()
        initView(view)

        super.onViewCreated(view, savedInstanceState)
    }


    private fun initView(view: View){
        view.apply {
            viewButton = findViewById(R.id.gridOrList)
            loadingView = findViewById(R.id.loading)
            viewButton.setOnClickListener {
                listViewType = !listViewType
                setupViewMode()
                listenViewModel()
            }
            setupViewMode()
            listenViewModel()
        }
    }

    private fun setupViewMode() {
        var layoutManager: LinearLayoutManager? = null
        if (!listViewType) {
            viewButton.setImageResource(R.drawable.ic_list_view)
            heartResource = R.drawable.heart_shape_grey
            layoutManager = GridLayoutManager(requireContext(),3)
        } else {
            viewButton.setImageResource(R.drawable.ic_grid_24dp)
            heartResource = R.drawable.heart_shape_outlined
            layoutManager = LinearLayoutManager(requireContext())
        }

        recyclerView = binding.recycler
        recyclerView.layoutManager = layoutManager
        // Set the adapter to the RecyclerView
        recyclerView.adapter = movieRecyclerAdapter
    }

    override fun onStart(){
        super.onStart()
        viewModel.updateLikedMovieIds()
        listenViewModel()
    }
    private fun listenViewModel() {
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateMovieList(it)
            }
            liveDataGenreList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.sendGenreList(it)
            }
            liveDataViewType.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateViewType(listViewType)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                recyclerView.visibility = if (it) View.GONE else View.VISIBLE
            }
            liveDataLikedMovieIds.observe(viewLifecycleOwner) {
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
                override fun onMovieClick(position: Int, movieView : View,  movieList: MutableList<MovieDetail>) {
                    movieClicked(position, movieView, movieList)
                }

                override fun onHeartButtonClick(
                    adapterPosition: Int,
                    movieView: View,
                    results: MutableList<MovieDetail>,
                    heartButton : ImageButton
                ) {
                    heartButtonClicked(adapterPosition, movieView, results, heartButton)


                }

            })

        }
    }

    private fun movieClicked(position: Int, movieView:View, movieList: MutableList<MovieDetail>) {
        val clickedMovie = movieList[position]
        val id = clickedMovie.id
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_ID, id)
            putInt(BundleKeys.position, position)
        }

        val destinationFragment = DetailMovieFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_detail, bundle) // R.id.action_detail

    }

    private fun heartButtonClicked(
        position: Int,
        itemView: View,
        results: MutableList<MovieDetail>,
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