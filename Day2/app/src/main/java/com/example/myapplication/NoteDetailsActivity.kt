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

        if (intent.hasExtra("detail_note")) {
            val detail = intent.getStringExtra("detail_note")
            tvNoteDetails.text = detail
        }
    }
}
