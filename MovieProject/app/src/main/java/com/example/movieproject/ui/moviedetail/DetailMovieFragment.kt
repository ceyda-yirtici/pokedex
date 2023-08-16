package com.example.movieproject.ui.moviedetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentDetailBinding
import com.example.movieproject.model.CastPerson
import com.example.movieproject.model.MovieDetail
import com.example.movieproject.ui.FavoritesManager
import com.example.movieproject.ui.cast.CastFragment
import com.example.movieproject.utils.BundleKeys
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var favoritesManager: FavoritesManager
    private val viewModel: DetailsViewModel by viewModels(ownerProducer = { this })
    private var castRecyclerAdapter: CastRecyclerAdapter = CastRecyclerAdapter()
    private lateinit var movieDetail:MovieDetail

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesManager = FavoritesManager.getInstance(viewModel.getMovieDao())
        initView(view)
        listenViewModel()
    }
    override fun onStart(){
        super.onStart()

        val id : Int = requireArguments().getInt(BundleKeys.REQUEST_MOVIE_ID)
        viewModel.displayMovie(id)
        viewModel.displayCast(id)
        listenViewModel()
    }
    private fun initView(view: View) {
        view.apply {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = castRecyclerAdapter
        }
    }


    private fun listenViewModel() {

        viewModel.apply {
            liveDataMovie.observe(viewLifecycleOwner) {
                updateMovie(it)
                movieDetail = it
                binding.heartInDetail.setOnClickListener(
                     onHeartButtonClick(it)
                )

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
            liveDataCast.observe(viewLifecycleOwner)  {
                castRecyclerAdapter.updateList(it)
            }
            binding.toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


    }

    private fun navigateCast(position: Int, itemList: MutableList<CastPerson>) {
        val clickedPerson = itemList[position]
        val id = clickedPerson.id
        val bundle = Bundle().apply {
            putInt(BundleKeys.REQUEST_PERSON_ID, id)
            putInt(BundleKeys.position, position)
            putString(BundleKeys.PHOTO_URL, movieDetail.backdrop_path)
        }

        val destinationFragment = CastFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_cast, bundle) // R.id.action_detail
    }


    private fun onHeartButtonClick(movieDetail: MovieDetail): View.OnClickListener {
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
            Glide.with(this@DetailMovieFragment).load(BundleKeys.baseImageUrlForOriginalSize + photoUrl).
            listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                    Log.e("glide", "onLoadFailed")
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    Log.d("glide", "OnResourceReady")
                    binding.detailPhoto.visibility = View.VISIBLE
                    binding.releaseDate.visibility = View.VISIBLE
                    binding.movieDescription.visibility = View.VISIBLE
                    binding.title.visibility = View.VISIBLE
                    binding.heartInDetail.visibility =View.VISIBLE
                    binding.star.visibility = View.VISIBLE
                    binding.voteText.visibility =  View.VISIBLE
                    binding.recycler.visibility = View.VISIBLE
                    binding.genreContainer.visibility = View.VISIBLE
                    binding.cast.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                    binding.toolbar.visibility = View.VISIBLE
                    return false
                }
            })
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


}


