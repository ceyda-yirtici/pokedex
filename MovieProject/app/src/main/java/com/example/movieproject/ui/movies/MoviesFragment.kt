package com.example.movieproject.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentMoviesBinding
import com.example.movieproject.model.MovieDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies), MovieRecyclerAdapter.OnClickListener{

    private var _binding: FragmentMoviesBinding? = null
    private var pageCount = 1
    private val viewModel by viewModels<MoviesViewModel>()
    private val movieRecyclerAdapter= MovieRecyclerAdapter(this@MoviesFragment)

    private lateinit var recyclerView: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        listenViewModel()
    }

    private fun listenViewModel() {
        viewModel.apply {
            liveDataMovieList.observe(viewLifecycleOwner) {
                movieRecyclerAdapter.updateList(it)
             }
        }
    }

    private fun initView(view: View){
        view.apply {
            recyclerView = findViewById(R.id.recycler)
            recyclerView.adapter = movieRecyclerAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    Log.e("initview", "you reached end")
                    pageCount++
                    viewModel.setPageNumber(pageCount)
                    viewModel.displayGroup(pageCount)

                }
            })
        }
    }

    /*
    movieRecyclerAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
        @Override
        public void onBottomReached(int position) {
            //your code goes here
        }
    })*/

    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    override fun onMovieClick(adapterPosition: Int, movieList: ArrayList<MovieDetail>) {

    }

}