package com.example.movieproject.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentDiscoverBinding
import com.example.movieproject.ui.discovered.DiscoveredFragment
import com.example.movieproject.utils.BundleKeys
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var dialogView: View // only for genre dialog
    private lateinit var chipGroupDialog: ChipGroup // only for genre dialog
    private val viewModel: DiscoverViewModel by viewModels(ownerProducer = { this })
    private var chipGroup : MutableList<String> = mutableListOf()
    private var selectedChips: MutableList<String> = mutableListOf()
    private var seekBarResult: Float = 0.0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        dialogView = LayoutInflater.from(requireContext()).inflate(
        R.layout.genre_dialog_layout, null)
        chipGroupDialog = dialogView.findViewById(R.id.chipGroupDialog)
        chipGroupDialog.removeAllViews()
        selectedChips = arrayListOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenViewModel()
    }


    private fun listenViewModel() {

        viewModel.apply {
            genreOptionList.observe(viewLifecycleOwner) {
                chipGroup = it

            }
            binding.chipButton.setOnClickListener { showChipSelectionDialog() }
            binding.discoverButton.setOnClickListener {
                onDiscoverButtonClicked()
            }
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // This method is called when the SeekBar's progress changes
                    // You can perform actions based on the progress value here
                    seekBarResult = progress.toFloat()/2
                    binding.degreeText.text = seekBarResult.toString()
                    binding.degreeText.translationX = (progress * 45).toFloat()

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // This method is called when the user starts interacting with the SeekBar
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // This method is called when the user stops interacting with the SeekBar
                }
            })

        }



    }

    private fun onDiscoverButtonClicked() {
        var ids: String = ""
        viewModel.liveDataGenreList.observe(viewLifecycleOwner){
            ids = it.filterValues { it in selectedChips }.keys.toMutableList().joinToString(separator = ",")
        }
        val bundle = Bundle().apply {
            putString(BundleKeys.REQUEST_DISCOVER, ids)
            putFloat(BundleKeys.MIN_VOTE, seekBarResult)
        }

        val destinationFragment = DiscoveredFragment()
        destinationFragment.arguments = bundle
        findNavController().navigate(R.id.discoveredFragment, bundle) // R.id.action_detail

        chipGroupDialog.removeAllViews()
        selectedChips = arrayListOf()
    }

    fun createChip(text: String, isCheckable: Boolean): Chip {
        val chip = Chip(requireContext())
        chip.text = text
        chip.isCheckable = isCheckable
        return chip
    }

    private fun showChipSelectionDialog() {

        dialogView = LayoutInflater.from(requireContext()).inflate(
            R.layout.genre_dialog_layout, null)
        chipGroupDialog = dialogView.findViewById(R.id.chipGroupDialog)
        val chipButton: Chip = binding.chipButton
        chipGroupDialog.removeAllViews() // Clear previous chips
        val chipGroupView: ChipGroup = binding.chipGroup
        for (title in chipGroup) {
            val chip = createChip(title, true)
            if(title in selectedChips) chip.isChecked = true
            if (chip !== chipButton) {
                chipGroupDialog.addView(chip)
            }
        }

        // Build and show the dialog
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Select Options")
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, which ->
                selectedChips = mutableListOf()
                for (index in 0 until chipGroupDialog.childCount) {
                    val chip = chipGroupDialog.getChildAt(index) as Chip
                    if (chip.isChecked) {
                        selectedChips.add(chip.text.toString())
                    }
                }

                // Update the selected chips in your UI
                updateSelectedChipsUI()
            }
            .setNegativeButton("Cancel", null)
            .create() // Create the dialog instance

        alertDialog.show() // Show the dialog
    }

    private fun updateSelectedChipsUI() {
        // Add selected chips to your UI, e.g., ChipGroup
        val chipGroupView: ChipGroup = binding.chipGroup
        val chipButton: Chip = binding.chipButton
        chipGroupView.removeAllViews()
        chipGroupView.addView(chipButton)
        for (chipText in selectedChips) {
            val chip = Chip(requireContext())
            chip.text = chipText
            chipGroupView.addView(chip)
        }
    }
}