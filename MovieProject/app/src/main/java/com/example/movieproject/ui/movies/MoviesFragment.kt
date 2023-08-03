package com.example.movieproject.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentMoviesBinding
import com.example.movieproject.model.MovieDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies){

    private var _binding: FragmentMoviesBinding? = null
    private var pageCount = 1
    private val viewModel by viewModels<MoviesViewModel>()
    private val movieRecyclerAdapter= MovieRecyclerAdapter()

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
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    Log.e("initview", "you reached end")
                    pageCount++
                    viewModel.setPageNumber(pageCount)
                    viewModel.displayGroup(pageCount)

                }
            })
            movieRecyclerAdapter.setOnClickListener(object : MovieRecyclerAdapter.OnClickListener{
                override fun onMovieClick(position: Int, pokeList: ArrayList<MovieDetail>) {
                    TODO("Not yet implemented")
                }

                override fun onHeartButtonClick(adapterPosition: Int, movieView: View) {
                    heartButtonClicked(adapterPosition,movieView)
                }

            })

        }
    }

    private fun initView(view: View){
        view.apply {
            recyclerView = findViewById(R.id.recycler)
            recyclerView.adapter = movieRecyclerAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager


        }
        viewModel.liveDataGenreList.observe(viewLifecycleOwner) {
            movieRecyclerAdapter.sendGenreList(it)
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



    private fun heartButtonClicked(adapterPosition: Int, itemView: View){
        val heartButton: ImageButton = itemView.findViewById(R.id.heart_in_list)
        val filledHeartDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.heart_shape_red)

        if (heartButton.tag == null) {
            heartButton.setImageResource(R.drawable.heart_shape_red)
            heartButton.tag = "filled"
        } else {
            if (heartButton.tag == "filled") {
                heartButton.setImageResource(R.drawable.heart_shape_outlined)
                heartButton.tag = "outline"
            } else {
                heartButton.setImageResource(R.drawable.heart_shape_red)
                heartButton.tag = "filled"
            }
        }


    }



}