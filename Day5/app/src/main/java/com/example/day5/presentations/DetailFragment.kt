package com.example.day5.presentations

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.day5.R
import com.example.day5.service.PokeService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment() : Fragment(R.layout.fragment_detail) {

    @Inject lateinit var pokeService: PokeService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.detail).text = requireArguments().getString("message")
    }

    companion object {
        const val BUNDLE_KEY = "message"
    }

}