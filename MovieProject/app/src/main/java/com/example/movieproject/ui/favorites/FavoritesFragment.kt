package com.example.movieproject.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentFavoritesBinding
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.GenreMapper
import com.example.movieproject.ui.moviedetail.DetailMovieFragment
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.movieproject.utils.BundleKeys
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var movieDao: MovieDao
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels(ownerProducer = { this })

    private var movieRecyclerAdapter: MovieRecyclerAdapter = MovieRecyclerAdapter()
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var loadingView: ProgressBar
    private lateinit var recyclerView: RecyclerView


    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext())
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao(), viewModel.viewModelScope)
        val navController = findNavController()
        viewModel.displayGroup()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    composeView.setContent {
                        FavoritesScreen(
                            uiState,
                            navController,
                            favoritesManager
                        )
                    }
                }

            }
        }
        return composeView
    }
}/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbarFavorites)
        toolbarTitle.text = "Favorite Movies"

        val database = AppDatabaseProvider.getAppDatabase(requireActivity().application)
        movieDao = database.movieDao()
        favoritesManager = FavoritesManager.getInstance(movieDao, viewModel.viewModelScope)
        initView(view)
        listenViewModel()
    }

    override fun onStart(){
        super.onStart()
        viewModel.createList()

    }
    private fun initView(view: View) {
        view.apply {
            recyclerView = binding.recycler
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager

            // Create the adapter instance

            // Set the adapter to the RecyclerView
            recyclerView.adapter = movieRecyclerAdapter

            loadingView = binding.loading
        }
    }



    private fun listenViewModel() {
        viewModel.apply {
            liveDataFavoritesList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateFavList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                recyclerView.visibility = if (it) View.GONE else View.VISIBLE
            }
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    viewModel.displayGroup()
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
                    heartButton: ImageButton
                ) {
                    heartButtonClicked(adapterPosition, movieView, results)


                }

            })

        }
    }






    private fun movieClicked(position: Int, movieView:View, movieList: MutableList<MovieDetail>) {
        val clickedMovie = movieList[position]
        val id = clickedMovie.id
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_MOVIE_ID, id)
            putInt(BundleKeys.position, position)
        }

        val destinationFragment = DetailMovieFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_detail, bundle) // R.id.action_detail

    }

    private fun heartButtonClicked(position: Int, itemView: View, results: MutableList<MovieDetail>){
        val heartButton: ImageButton = itemView.findViewById(R.id.heart_in_detail)

        val clickedMovie = results[position]

        if ( clickedMovie.heart_tag == "filled") {
            heartButton.setImageResource(R.drawable.heart_shape_outlined)
            clickedMovie.heart_tag = "outline"
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    favoritesManager.removeMovieFromDB(
                        clickedMovie,
                        heartButton
                    )
                }
            }
        }
        else {
            heartButton.setImageResource(R.drawable.heart_shape_red)
            clickedMovie.heart_tag = "filled"
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    favoritesManager.addMovieToDB(
                        clickedMovie,
                        heartButton
                    )
                }
            }
        }

    }
}*/