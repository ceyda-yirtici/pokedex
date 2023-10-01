package com.example.movieproject.ui

import com.example.movieproject.model.MovieDetail
import com.example.myapplication.room.MovieDao

class GenreMapper {

    companion object {

        var genreMapper : HashMap<Int, String> = hashMapOf()

        fun sendGenreList(it: HashMap<Int, String>) {
            genreMapper = it
        }

        fun map(item: MovieDetail): ArrayList<String> {
            val genreNamesOfTheMovies = arrayListOf<String>()
                for (genreId in item.genre_ids) {
                    val genreName = genreMapper[genreId]
                    if (genreName != null) {
                        genreNamesOfTheMovies.add(genreName)
                    }
                }

            return genreNamesOfTheMovies
        }


    }


}