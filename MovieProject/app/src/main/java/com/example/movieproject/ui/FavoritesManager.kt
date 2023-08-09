package com.example.movieproject.ui

import com.example.myapplication.room.MovieDao
import javax.inject.Singleton

@Singleton
class FavoritesManager {

    private lateinit var movieDao: MovieDao

//singleton yap
/*
    init {
        val database = AppDatabaseProvider.getAppDatabase(requireActivity().application)
        movieDao = database.movieDao()

        initView(view)
        listenViewModel()
    }*/
}