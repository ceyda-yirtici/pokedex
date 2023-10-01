package com.example.movieproject.ui.discover

import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.movieproject.R
import com.example.movieproject.ui.theme.MovieTheme
import com.example.movieproject.utils.BundleKeys

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(uiState: DiscoverViewModel.DiscoverMoviesUiState, navController: NavController) {
    val chipGroup by remember { mutableStateOf(uiState.genreOptionList) }
    val selectedChips = remember { mutableStateListOf<String>() }
    var seekBarResult by remember { mutableStateOf(0.0F) }


    val bundle by remember { mutableStateOf(Bundle())}

    Column() {
        Box(modifier = Modifier) {
            TopAppBar(
                title = {
                    Text(text = "Discovered Movies",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MovieTheme.colors.textPrimary)
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MovieTheme.colors.uiBackground
                ),
                modifier = Modifier
                    .zIndex(4f)
                    .background(MovieTheme.colors.uiBorder)
                    .fillMaxWidth()
                    .align(Alignment.Center),
            )
        }
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
            SeekBar(
                value = seekBarResult,
                onValueChange = { seekBarResult = it },
                valueRange = 0F..10F,
                steps = 0.5F
            )
            Text(
                text = seekBarResult.toString().subSequence(0,3).toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                color= MovieTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChipGroup(
                chipGroup = chipGroup,
                selectedChips = selectedChips,
                onChipSelected = { chipText, selected ->
                    if (selected) {
                        selectedChips.add(chipText)
                    } else {
                        selectedChips.remove(chipText)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    bundle.apply {
                        putString(BundleKeys.REQUEST_DISCOVER, selectedChips.joinToString(separator = ","))
                        putFloat(BundleKeys.MIN_VOTE, seekBarResult)
                    }
                    navController.navigate(R.id.action_discovered, bundle)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Discover")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChipGroup(
    chipGroup: List<String>,
    selectedChips: MutableList<String>,
    onChipSelected: (String, Boolean) -> Unit
) {
    FlowRow {
        for (chipText in chipGroup) {
            val isSelected = selectedChips.contains(chipText)
            Chip(
                onClick = {
                    onChipSelected(chipText, !isSelected)
                },
                modifier = Modifier.padding(4.dp),
                enabled = true, // You can customize this based on your logic
                interactionSource = remember { MutableInteractionSource() },
                shape = RoundedCornerShape(16.dp), // Customize the shape as needed
                border = if (isSelected) {
                    BorderStroke(2.dp, Color.Blue) // Customize the border color and width
                } else {
                    null
                },
                colors = ChipDefaults.chipColors( // You can customize colors as needed
                    backgroundColor = if (isSelected) Color.Blue else Color.Gray,
                    contentColor = Color.White
                ),
                leadingIcon = null, // You can add an icon if needed
            ) {
                Text(
                    text = chipText,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun SeekBar(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Float
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps.toInt()
    )
}