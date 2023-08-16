package com.example.movieproject.ui.movies

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentMoviesBinding
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.moviedetail.DetailMovieFragment
import com.example.movieproject.utils.BundleKeys
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.MovieRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MoviesFragment : Fragment(){

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var favoritesManager: FavoritesManager
    private var pageCount = 1
    private val viewModel: MoviesViewModel by viewModels(ownerProducer = { this })
    private var movieRecyclerAdapter: MovieRecyclerAdapter = MovieRecyclerAdapter()
    private var heartResource: Int = R.drawable.heart_shape_grey
    private var listViewType = true
    private var userQuery = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("view", "create")
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.findViewById<TextView>(R.id.toolbarTitle).text = "Popular Movies"
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao())
        initView(view)

    }




    private fun initView(view: View){
        view.apply {

            binding.toolbar.findViewById<ImageButton>(R.id.gridOrList).setOnClickListener {
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
            binding.toolbar.findViewById<SearchView>(R.id.searchView).visibility = View.GONE
            binding.toolbar.findViewById<ImageButton>(R.id.gridOrList).setImageResource(R.drawable.ic_list_view)
            heartResource = R.drawable.heart_shape_grey
            val orientation = resources.configuration.orientation
            var spanCount = 3
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Device is in landscape mode
                spanCount = 6
            }
            layoutManager = GridLayoutManager(requireContext(), spanCount)
        } else {
            binding.toolbar.findViewById<SearchView>(R.id.searchView).visibility = View.VISIBLE
            binding.toolbar.findViewById<ImageButton>(R.id.gridOrList).setImageResource(R.drawable.ic_grid_24dp)
            heartResource = R.drawable.heart_shape_outlined
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.recycler.layoutManager = layoutManager
        // Set the adapter to the RecyclerView
        binding.recycler.adapter = movieRecyclerAdapter
    }

    override fun onStart(){
        super.onStart()
        viewModel.updateLikedMovieIds()
        listenViewModel()
    }
    override fun onResume(){
        super.onResume()
        viewModel.updateLikedMovieIds()
        listenViewModel()
    }


    private fun listenViewModel() {
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateMovieList(it)
            }
            liveDataGenreList.observe(viewLifecycleOwner) {
                MovieRecyclerAdapter.sendGenreList(it)
            }
            liveDataViewType.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateViewType(listViewType)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                binding.recycler.visibility = if (it) View.GONE else View.VISIBLE
            }
            liveDataLikedMovieIds.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.setLikedMovieIds(it)
            }
            binding.swiperefresh.setOnRefreshListener {
                setUpListComponents()
                binding.swiperefresh.isRefreshing  = false
                viewModel.displayGroup(pageCount)
            }
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    Log.e("initview", "you reached end")
                    pageCount++
                    viewModel.setPageNumber(pageCount)
                    if( binding.toolbar.findViewById<TextView>(R.id.toolbarTitle).visibility == View.VISIBLE)
                        viewModel.displayGroup(pageCount)
                    else viewModel.searchMovies(userQuery, pageCount)

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
            binding.toolbar.findViewById<SearchView>(R.id.searchView).setOnSearchClickListener {
                binding.toolbar.findViewById<TextView>(R.id.toolbarTitle).visibility = View.GONE
                binding.toolbar.findViewById<ImageButton>(R.id.gridOrList).visibility = View.GONE
                setUpListComponents()
            }
            binding.toolbar.findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object: SearchView.OnQueryTextListener  {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        viewModel.searchMovies(query, pageCount)
                        userQuery =  query
                        setUpListComponents()
                    }
                    return false
                }


                override fun onQueryTextChange(query: String?): Boolean {
                    if (query != null && query != "") {
                        viewModel.searchMovies(query, pageCount)
                        userQuery =  query

                        binding.toolbar.findViewById<TextView>(R.id.toolbarTitle).visibility = View.GONE
                        binding.toolbar.findViewById<ImageButton>(R.id.gridOrList).visibility = View.GONE
                        setUpListComponents()
                    }
                    return false
                }
            })
            binding.toolbar.findViewById<SearchView>(R.id.searchView).setOnCloseListener {
                binding.toolbar.findViewById<TextView>(R.id.toolbarTitle).visibility = View.VISIBLE
                binding.toolbar.findViewById<ImageButton>(R.id.gridOrList).visibility = View.VISIBLE
                setUpListComponents()
                viewModel.displayGroup(pageCount)

                false
            }

        }
    }

    private fun setUpListComponents() {
        listViewType = true
        viewModel.setLiveDataMovieList(mutableListOf())
        pageCount = 1
        viewModel.setPageNumber(pageCount)
    }

    private fun movieClicked(position: Int, movieView:View, movieList: MutableList<MovieDetail>) {
        val clickedMovie = movieList[position]
        val id = clickedMovie.id
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_MOVIE_ID, id)
            putInt(BundleKeys.position, position)
            putInt(BundleKeys.ACTION_ID, 1)
            putParcelable(BundleKeys.SAVED_SUPER_STATE,
                binding.recycler.layoutManager?.onSaveInstanceState()
            )
        }

        viewModel.setLiveDataMovieList(movieList)
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
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    favoritesManager.removeMovieFromDB(
                        clickedMovie,
                        heartButton
                    )
                }
            }
        } else {
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




}

