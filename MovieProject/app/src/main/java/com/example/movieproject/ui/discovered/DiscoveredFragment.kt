package com.example.movieproject.ui.discovered

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.discover.DiscoveredViewModel
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoveredFragment : Fragment() {

    private lateinit var favoritesManager: FavoritesManager
    private val viewModel: DiscoveredViewModel by viewModels(ownerProducer = { this })
    private var genres: String = ""
    private var minVote = 0.0F


    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val composeView = ComposeView(requireContext())
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao(), viewModel.viewModelScope)
        val navController = findNavController()


        genres = requireArguments().getString(BundleKeys.REQUEST_DISCOVER).toString()
        minVote = requireArguments().getFloat(BundleKeys.MIN_VOTE)
        viewModel.displayGroup(genres, minVote)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    composeView.setContent {
                        DiscoveredScreen(
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

    /*

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = binding.toolbarDiscovered
        toolbarTitle.text = "Discover"
        favoritesManager = FavoritesManager.getInstance(
            viewModel.getDao(),
            viewModel.viewModelScope
        )
        initView(view)
        listenViewModel()
    }

    private fun initView(view: View) {
        view.apply {
            val layoutManager = LinearLayoutManager(requireContext())
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = movieRecyclerAdapter
        }
    }


    override fun onStart(){
        super.onStart()
        genres = requireArguments().getString(BundleKeys.REQUEST_DISCOVER).toString()
        minVote = requireArguments().getFloat(BundleKeys.MIN_VOTE)
        viewModel.updateLikedMovieIds()
        viewModel.displayGroup(pageCount, genres, minVote)

    }



    private fun listenViewModel() {
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateMovieList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                binding.recycler.visibility = if (it) View.GONE else View.VISIBLE
            }
            liveDataLikedMovieIds.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.setLikedMovieIds(it)
            }
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_discoveredFragment_to_navigation_discover)
            }
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    pageCount++
                    viewModel.displayGroup(pageCount, genres, minVote)
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
            putInt(BundleKeys.ACTION_ID, 3)
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

    }*/
}