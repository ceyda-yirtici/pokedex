package com.example.day5.presentations

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.day5.R
import com.example.day5.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel by viewModels<ListViewModel>()

    private val projectRecyclerAdapter = ProjectRecyclerAdapter()

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
            recyclerView.adapter = projectRecyclerAdapter
            viewModel.displayGroup()
            loadingView = findViewById(R.id.loading)
        }
    }

    private fun listenViewModel() {
        viewModel.apply {
            liveDataPokeGroup.observe(viewLifecycleOwner) {
                projectRecyclerAdapter.updateList(it)
            }
            liveDataLoading.observe(viewLifecycleOwner) {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                recyclerView.visibility = if (it) View.GONE else View.VISIBLE
            }
        }
    }

}