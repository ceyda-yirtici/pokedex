package com.example.day5.presentations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.day5.utils.BundleKeys
import com.example.day5.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BundleKeys.BUNDLE_KEY
    }
}