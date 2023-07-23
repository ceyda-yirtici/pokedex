package com.example.myapplication;

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.myapplication.R

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var tvNoteDetails: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        tvNoteDetails = findViewById(R.id.second)

        // Get the data from the intent
        if (intent.hasExtra("detail_note")) {
            val detail = intent.getStringExtra("detail_note")
            // You can use this title to fetch the note details from your data source
            // For now, let's just display the title itself as the details
            tvNoteDetails.text = detail
        }
    }
}
