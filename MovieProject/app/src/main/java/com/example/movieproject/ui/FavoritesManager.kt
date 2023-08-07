package com.example.movieproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentMoviesBinding
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.ui.movies.MovieRecyclerAdapter
import com.example.movieproject.ui.movies.MoviesViewModel
import com.example.myapplication.room.MovieDao

class FavoritesManager {

    private lateinit var movieDao: MovieDao


/*
    init {
        val database = AppDatabaseProvider.getAppDatabase(requireActivity().application)
        movieDao = database.movieDao()

        initView(view)
        listenViewModel()
    }*/
}