package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentNoteDetailsBinding


private lateinit var binding: FragmentNoteDetailsBinding


class NoteDetailsFragment : Fragment(R.layout.fragment_note_details) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        val arguments = arguments
        if (arguments != null) {
            val detail = arguments.getString(REQUEST_DETAIL)
            val tag = arguments.getString(REQUEST_KEY_TAG)
            val title = arguments.getString(REQUEST_KEY_TITLE)
            binding.details.text = detail
            binding.category.text = tag
            binding.headline.text = title
        }
        return binding.root
    }



    companion object {
        const val REQUEST_DETAIL = "detail"
        const val REQUEST_KEY_TAG = "tag"
        const val REQUEST_KEY_TITLE = "title"
    }
}