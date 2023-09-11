package com.example.movieproject.ui.moviedetail

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
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {
    private val viewModel: DetailsViewModel by viewModels(ownerProducer = { this })
    private lateinit var favoritesManager: FavoritesManager

        @SuppressLint("UnsafeRepeatOnLifecycleDetector")
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val composeView = ComposeView(requireContext())
            favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao(), viewModel.viewModelScope)
            val id: Int = requireArguments().getInt(BundleKeys.REQUEST_PERSON_ID)
            viewModel.displayCast(id)
            viewModel.displayMovie(id)
            viewModel.displayRecs(id)
            val navController = findNavController()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { uiState ->
                        composeView.setContent {
                            MovieScreen(movieUiState = uiState,
                                requireActivity().onBackPressedDispatcher,
                                navController, favoritesManager)
                        }
                    }
                }
            }
            return composeView

    }
}


