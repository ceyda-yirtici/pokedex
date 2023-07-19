package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteClickListener {

    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = arrayListOf<RecyclerItem>().apply {
            add(RecyclerItem("OBSS","job", " " ))
            add(RecyclerItem("Android","hobby", " " ))
            add(RecyclerItem("alışveriş","food", " " ))

            add(RecyclerItem("OBSS","job", " " ))
            add(RecyclerItem("kitap oku","hobby", " " ))
            add(RecyclerItem("alışveriş","yemek", " " ))

            add(RecyclerItem("OBSS","job", " " ))
            add(RecyclerItem("Android","hobby", " " ))
            add(RecyclerItem("tarif linki","food", " " ))
        }
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NoteAdapter(list, this@MainActivity)

    }
    override fun onNoteClick(position: Int, notes: List<RecyclerItem>) {
        // Handle item click here
        val clickedNote = notes[position]
        val title = clickedNote.name

        // Navigate to a new page (e.g., NoteDetailsActivity) and pass the data
        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra("note_title", title)
        startActivity(intent)
    }
}