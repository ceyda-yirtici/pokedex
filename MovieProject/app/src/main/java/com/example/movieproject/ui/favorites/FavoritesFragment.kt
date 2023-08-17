package com.example.movieproject.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentFavoritesBinding
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.moviedetail.DetailMovieFragment
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesManager: FavoritesManager
    private val viewModel: FavoritesViewModel by viewModels(ownerProducer = { this })
    private var movieRecyclerAdapter: MovieRecyclerAdapter = MovieRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = binding.toolbar.findViewById<TextView>(R.id.toolbarFavorites)
        toolbarTitle.text = "Favorite Movies"
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao())
        initView(view)
        listenViewModel()
    }

    override fun onStart(){
        super.onStart()
        viewModel.createList()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        listenViewModel()
    }


    private fun initView(view: View) {
        view.apply {
            val layoutManager = LinearLayoutManager(requireContext())
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = movieRecyclerAdapter
        }
    }



    private fun listenViewModel() {
        viewModel.apply {
            liveDataFavoritesList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateFavList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                binding.recycler.visibility = if (it) View.GONE else View.VISIBLE
            }
            binding.swiperefresh.setOnRefreshListener {
                viewModel.createList()
                binding.swiperefresh.isRefreshing  = false
                viewModel.displayGroup()
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
            putInt(BundleKeys.ACTION_ID, 2)
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
}