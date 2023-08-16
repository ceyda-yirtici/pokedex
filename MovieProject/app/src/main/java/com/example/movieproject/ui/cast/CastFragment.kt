package com.example.movieproject.ui.cast

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentCastBinding
import com.example.movieproject.model.CastPerson
import com.example.movieproject.ui.MovieRecyclerAdapter
import com.example.movieproject.utils.BundleKeys

class CastFragment  : Fragment(R.layout.fragment_cast) {

    private lateinit var binding: FragmentCastBinding
    private val viewModel: CastViewModel by viewModels(ownerProducer = { this })
    private var movieRecyclerAdapter: MovieRecyclerAdapter = MovieRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        listenViewModel()
    }
    override fun onStart(){
        super.onStart()

        val id : Int = requireArguments().getInt(BundleKeys.REQUEST_PERSON_ID)
        viewModel.displayCast(id)
        listenViewModel()
    }
    private fun initView(view: View) {
        view.apply {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = movieRecyclerAdapter
        }
    }


    private fun listenViewModel() {

        viewModel.apply {
            liveDataCast.observe(viewLifecycleOwner) {
                updateCast(it)


            }
            binding.toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


    }



    @SuppressLint("SetTextI18n")
    private fun updateCast(it: CastPerson) {

        binding.title.text = it.name
        binding.biography.text = it.biography
        binding.age.text = "Born: " + it.birthday
        binding.career.text = it.known_for_department
        val photo = binding.backdropCastPhoto

        Glide.with(this@CastFragment).load(BundleKeys.baseImageUrlForOriginalSize + requireArguments().getString(BundleKeys.PHOTO_URL)).
        listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                Log.e("glide", "onLoadFailed")
                return false
            }
            override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                Log.d("glide", "OnResourceReady")
                binding.biography.visibility = View.VISIBLE
                binding.age.visibility = View.VISIBLE
                binding.career.visibility = View.VISIBLE
                binding.title.visibility = View.VISIBLE
                binding.recycler.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
                binding.toolbar.visibility = View.VISIBLE
                return false
            }
        })
            .into(photo)

    }


}



