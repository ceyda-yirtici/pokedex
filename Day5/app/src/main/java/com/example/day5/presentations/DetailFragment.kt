package com.example.day5.presentations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.day5.R
import com.example.day5.service.PokeService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment() : Fragment(R.layout.fragment_detail) {

    @Inject lateinit var pokeService: PokeService

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.name).text = requireArguments().getString("name")

        val photo: ImageView = view.findViewById(R.id.photo)
        Glide.with(photo).load(requireArguments().getString("photo")).into(photo)

        val powers = requireArguments().getStringArrayList("powers")

        if (powers != null) {
            val powersText = "Abilities: " + powers.joinToString(", ")
            view.findViewById<TextView>(R.id.abilities).text = powersText
        }
        view.findViewById<TextView>(R.id.height).text = "Height: " + requireArguments().getString("height")
        view.findViewById<TextView>(R.id.weight).text = "Weight: " +requireArguments().getString("weight")

    }

    companion object {
        const val POWERS = "powers"
        const val PHOTO = "photo"
        const val NAME = "name"
        const val HEIGHT = "height"
        const val WEIGHT = "weight"
    }

}