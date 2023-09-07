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
import androidx.navigation.fragment.findNavController
import com.example.movieproject.R
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail) {
    private val viewModel: DetailsViewModel by viewModels(ownerProducer = { this })
    private var pageCount = 1



        @SuppressLint("UnsafeRepeatOnLifecycleDetector")
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val composeView = ComposeView(requireContext())

            val id: Int = requireArguments().getInt(BundleKeys.REQUEST_PERSON_ID)
            viewModel.displayCast(id)
            viewModel.displayMovie(id)
            viewModel.displayRecs(id, pageCount)
            val navController = findNavController()

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { uiState ->
                        composeView.setContent {
                            MovieScreen(movieUiState = uiState,
                                requireActivity().onBackPressedDispatcher,
                                navController,)
                        }
                    }
                }
            }
            return composeView

    }
    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao())
        initView(view)
        val id : Int = requireArguments().getInt(BundleKeys.REQUEST_MOVIE_ID)
        viewModel.displayMovie(id)
        viewModel.displayCast(id)
        viewModel.displayRecs(id, pageCount)
        listenViewModel()
    }

    private fun initView(view: View) {
        view.apply {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerCast.layoutManager = layoutManager
            binding.recyclerCast.adapter = castRecyclerAdapter
            val layoutManager2 = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerMovies.layoutManager = layoutManager2
            binding.recyclerMovies.adapter = movieRecyclerAdapter
            movieRecyclerAdapter.updateViewType(3)


        }
    }


    private fun listenViewModel() {

        viewModel.apply {
            liveDataMovie.observe(viewLifecycleOwner) {
                updateMovie(it)

                binding.heartInDetail.setOnClickListener(
                     heartButtonClicked(it)
                )
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                binding.loading.visibility = if (it) View.VISIBLE else View.GONE
                for (index in 0 until binding.content.childCount) {
                    val childView = binding.content.getChildAt(index)
                    childView.visibility = if (it) View.GONE else View.VISIBLE
                }
            }
            castRecyclerAdapter.setOnClickListener (object: CastRecyclerAdapter.OnClickListener{
                override fun onCastClick(
                    position: Int,
                    itemView: View,
                    itemList: MutableList<CastPerson>
                ) {
                    navigateCast(position,itemList)
                }

            })
            movieRecyclerAdapter.setOnBottomReachedListener(object : MovieRecyclerAdapter.OnBottomReachedListener{
                override fun onBottomReached(position: Int) {
                    Log.e("initview", "you reached end")
                    pageCount++
                    liveDataMovie.observe(viewLifecycleOwner){
                        viewModel.displayRecs(it.id, pageCount)
                    }

                }
            })
            movieRecyclerAdapter.setOnClickListener(object : MovieRecyclerAdapter.OnClickListener{
                override fun onMovieClick(position: Int, movieView : View,  movieList: MutableList<MovieDetail>) {
                    movieClicked(position, movieView, movieList)
                }

                override fun onHeartButtonClick(
                    adapterPosition: Int,
                    movieView: View,
                    movieList: MutableList<MovieDetail>,
                    heartButton: ImageButton
                ) {
                    return
                }

            })
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()


        }
    }

    private fun movieClicked(position: Int, movieView:View, movieList: MutableList<MovieDetail>) {
        val clickedMovie = movieList[position]
        val id = clickedMovie.id
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_MOVIE_ID, id)
            putInt(BundleKeys.position, position)
            putInt(BundleKeys.ACTION_ID, 5)

        }
        val destinationFragment = DetailMovieFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_detail, bundle) // R.id.action_detail
    }

    private fun navigateCast(position: Int, itemList: MutableList<CastPerson>) {
        val clickedPerson = itemList[position]
        val id = clickedPerson.id
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_PERSON_ID, id)
            putInt(BundleKeys.position, position)
            viewModel.liveDataMovie.observe(viewLifecycleOwner) {
                putString(BundleKeys.PHOTO_URL,it.backdrop_path)
            }
        }

        val destinationFragment = CastFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_cast, bundle) // R.id.action_detail
    }


    private fun heartButtonClicked(movieDetail: MovieDetail): View.OnClickListener {
        return View.OnClickListener {
            val heartButton = binding.heartInDetail

            if (movieDetail.heart_tag == "filled") {
                heartButton.setImageResource(R.drawable.heart_shape_outlined)
                movieDetail.heart_tag = "outline"
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        favoritesManager.removeMovieFromDB(
                            movieDetail,
                            heartButton
                        )
                    }
                }
            } else {
                heartButton.setImageResource(R.drawable.heart_shape_red)
                movieDetail.heart_tag = "filled"
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        favoritesManager.addMovieToDB(
                            movieDetail,
                            heartButton
                        )
                    }
                }
            }


        }
    }

    private fun updateMovie(it: MovieDetail) {

        binding.title.text = it.title
        binding.movieDescription.text = it.overview
        viewModel.liveDataMovieList.observe(viewLifecycleOwner) {
            movieRecyclerAdapter.updateMovieList(it)
            binding.recommendations.visibility =  if (it.size < 1) View.GONE else View.VISIBLE
        }
        viewModel.liveDataCast.observe(viewLifecycleOwner)  {
            castRecyclerAdapter.updateList(it)
            binding.cast.visibility = if (it.size < 1) View.GONE else View.VISIBLE
        }

        if (it.release_date.isNotEmpty())
            binding.releaseDate.text = it.release_date.subSequence(0,4)
        binding.voteText.text = it.vote.toString().subSequence(0,3)
        viewLifecycleOwner.lifecycleScope.launch {
            val count = withContext(Dispatchers.IO) {
                viewModel.getMovieDao().getCountById(requireArguments().getInt(BundleKeys.REQUEST_MOVIE_ID))
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
            Glide.with(this@DetailMovieFragment).load(BundleKeys.baseImageUrlForOriginalSize + photoUrl).centerCrop()
                .placeholder(R.drawable.baseline_photo_220dp) // drawable as a placeholder
                .error(R.drawable.baseline_photo_220dp) //  drawable if an error occurs
             .into(photo)

            val genreList : ArrayList<String> = arrayListOf()
            it.genres?.map {
                genreList.add(it.genre_name)
            }
            addingGenreView(genreList)
        }

    }
    private fun addingGenreView(genre_names: ArrayList<String>){

        val genreContainer: ChipGroup = binding.genreContainer

        // Clear any previous genres before adding new ones
        genreContainer.removeAllViews()
        for (genreName in genre_names) {
            Log.d("GenreAdapter", "Genre Name: $genreName")
            val genreView = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.item_genre, genreContainer, false)
            val genreTextView = genreView.findViewById<TextView>(R.id.genreTextView)
            genreTextView.textSize = genreTextSize
            genreTextView.text = genreName


            genreContainer.addView(genreView)
        }
    }
    companion object{
        const val genreTextSize = 15F
    }

*/
}


