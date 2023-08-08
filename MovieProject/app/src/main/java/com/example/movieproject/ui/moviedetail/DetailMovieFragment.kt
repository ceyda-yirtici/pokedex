package com.example.movieproject.ui.moviedetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentDetailBinding
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.room.AppDatabaseProvider
import com.example.movieproject.room.Movie
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import com.example.myapplication.room.MovieDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail) {
    @Inject
    lateinit var movieService: MovieService
    private lateinit var movieDao: MovieDao
    private val viewModel: DetailsViewModel by viewModels(ownerProducer = { this })
    private lateinit var binding: FragmentDetailBinding
    private var movieId: Int = -1
    private lateinit var loadingView: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)

        val database = AppDatabaseProvider.getAppDatabase(requireActivity().application)
        movieDao = database.movieDao()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieId = requireArguments().getInt(BundleKeys.REQUEST_ID)
        view.apply {
            loadingView = binding.loading
        }
        listenViewModel()
    }

    private fun listenViewModel() {

        viewModel.apply {
            liveDataMovie.observe(viewLifecycleOwner) {
                Log.e("display VM", it.toString())
                updateMovie(it)

                binding.heartInDetail.setOnClickListener(
                    onHeartButtonClick(it)
                )
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.detailPhoto.visibility = if (it) View.GONE else View.VISIBLE
                binding.releaseDate.visibility = if (it) View.GONE else View.VISIBLE

                binding.movieDescription.visibility = if (it) View.GONE else View.VISIBLE

                binding.title.visibility = if (it) View.GONE else View.VISIBLE

                binding.heartInDetail.visibility = if (it) View.GONE else View.VISIBLE
                loadingView.visibility = if (it) View.VISIBLE else View.GONE

            }
            displayMovie(movieId)
        }


    }

    private fun onHeartButtonClick(movieDetail: MovieDetail): View.OnClickListener {
        return View.OnClickListener {
            val heartButton = binding.heartInDetail

            if (movieDetail.heart_tag == "filled") {
                heartButton.setImageResource(R.drawable.heart_shape_outlined)
                movieDetail.heart_tag = "outline"
                removeMovieFromDB(movieDetail, heartButton)
            } else {
                heartButton.setImageResource(R.drawable.heart_shape_red)
                movieDetail.heart_tag = "filled"
                addMovieToDB(movieDetail, heartButton)
            }


        }
    }


    private fun updateMovie(it: MovieDetail) {

        binding.title.text = it.title
        binding.movieDescription.text = it.overview
        binding.releaseDate.text = it.release_date.subSequence(0,4)

        viewLifecycleOwner.lifecycleScope.launch {
            val count = withContext(Dispatchers.IO) {
                movieDao.getCountById(movieId)
            }
            if (count > 0) {
                binding.heartInDetail.setImageResource(R.drawable.heart_shape_red)
                it.heart_tag = "filled"
            } else {
                binding.heartInDetail.setImageResource(R.drawable.heart_shape_outlined)
                it.heart_tag = "outline"
            }
            val photo = binding.detailPhoto
            val photoUrl = it.backdrop_path
            Glide.with(photo).load(BundleKeys.baseImageUrlForOriginalSize + photoUrl).into(photo)
        }

    }
    private fun addMovieToDB(clickedMovie: MovieDetail, heartButton: ImageButton){

        val newMovie = Movie(movie_id = clickedMovie.id)
        lifecycleScope.launch {
            try {
                // Execute the database operation on the IO dispatcher
                withContext(Dispatchers.IO) {
                    movieDao.insert(newMovie)
                }

            } catch (e: Exception) {
                heartButton.setImageResource(R.drawable.heart_shape_outlined)
                clickedMovie.heart_tag = "outline"
            }
        }
    }
    private fun removeMovieFromDB(clickedMovie: MovieDetail, heartButton: ImageButton){

        lifecycleScope.launch {
            try {
                // Execute the database operation on the IO dispatcher
                withContext(Dispatchers.IO) {
                    movieDao.delete( viewModel.getMovieDao().get(clickedMovie.id))
                }

            } catch (e: Exception) {
                heartButton.setImageResource(R.drawable.heart_shape_red)
                clickedMovie.heart_tag = "filled"
            }
        }

    }


}


