package com.example.day4

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ListFragment : Fragment(R.layout.fragment_list){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button).setOnClickListener {

            //val bundle = bundleOf(BUNDLE_KEY to "merhaba")
            findNavController().navigate(R.id.action_listFragment_to_detailFragment) // R.id.action_detail
        }

    }
    companion object {
        const val BUNDLE_KEY = "message"
    }
}