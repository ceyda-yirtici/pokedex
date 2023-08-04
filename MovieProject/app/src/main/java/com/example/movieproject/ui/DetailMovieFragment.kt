package com.example.movieproject.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentDetailBinding
import com.example.movieproject.service.MovieService
import com.example.movieproject.utils.BundleKeys
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail) {
    @Inject
    lateinit var movieService: MovieService

    private lateinit var binding: FragmentDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.title.text = requireArguments().getString(BundleKeys.REQUEST_TITLE)
        binding.movieDescription.text = requireArguments().getString(BundleKeys.REQUEST_OVERVIEW)
        binding.releaseDate.text = requireArguments().getString(BundleKeys.REQUEST_DATE)?.subSequence(0,4)
        binding.heartInList.tag = requireArguments().getString(BundleKeys.REQUEST_HEART)

        if (binding.heartInList.tag == "filled") {
            binding.heartInList.setImageResource(R.drawable.heart_shape_red)
        } else {
            binding.heartInList.setImageResource(R.drawable.heart_shape_outlined)
        }




        val photo = binding.detailPhoto
        val photoUrl = requireArguments().getString(BundleKeys.REQUEST_PHOTO)
        Glide.with(photo).load(BundleKeys.baseImageUrlForOriginalSize + photoUrl).into(photo)
        Log.d("detail", BundleKeys.REQUEST_PHOTO)

        return binding.root

    }



}