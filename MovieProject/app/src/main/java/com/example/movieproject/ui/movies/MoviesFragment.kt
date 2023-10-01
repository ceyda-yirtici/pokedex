package com.example.movieproject.ui.movies

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
import com.example.movieproject.ui.GenreMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var favoritesManager: FavoritesManager
    private val viewModel: MoviesViewModel by viewModels(ownerProducer = { this })

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao(), viewModel.viewModelScope)
        val navController = findNavController()

        viewModel.displayGroup() // viewmodel ref

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    GenreMapper.sendGenreList(uiState.genreMapper)
                    composeView.setContent {
                        PopularMoviesScreen(
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
}