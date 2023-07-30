package com.example.day5.presentations

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.day5.R
import com.example.day5.model.CharacterDetail
import com.example.day5.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), PokeRecyclerAdapter.OnClickListener {

    private val viewModel by viewModels<ListViewModel>()

    private val pokeRecyclerAdapter = PokeRecyclerAdapter(this@ListFragment)

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        listenViewModel()
    }

    private fun initView(view: View) {
        view.apply {
            recyclerView = findViewById(R.id.recycler)
            recyclerView.adapter = pokeRecyclerAdapter
            viewModel.displayGroup()
            loadingView = findViewById(R.id.loading)
        }
    }

    private fun listenViewModel() {
        viewModel.apply {
            liveDataPokeGroup.observe(viewLifecycleOwner) {
                pokeRecyclerAdapter.updateList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                recyclerView.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
    }


    override fun onPokeClick(position: Int, pokeList : ArrayList<CharacterDetail> ) {

        val clickedPoke = pokeList[position]
        val name = clickedPoke.name
        val height = clickedPoke.height
        val weight = clickedPoke.weight
        val photo = clickedPoke.sprites.other.official_artwork.front_default

        val powers: List<String> = clickedPoke.abilities.map { it.ability.name }

        val bundle = Bundle().apply {
            putString(DetailFragment.NAME, name)
            putString(DetailFragment.HEIGHT, height.toString())
            putString(DetailFragment.WEIGHT, weight.toString())
            putString(DetailFragment.PHOTO, photo)
            putStringArrayList(DetailFragment.POWERS, ArrayList(powers))
        }

        val destinationFragment = DetailFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.action_detail, bundle) // R.id.action_detail
    }

}