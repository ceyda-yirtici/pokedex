import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.myapplication.R

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var tvNoteDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_bar)

       // tvNoteDetails = findViewById(R.id)

        // Get the data from the intent
        if (intent.hasExtra("note_title")) {
            val title = intent.getStringExtra("note_title")
            // You can use this title to fetch the note details from your data source
            // For now, let's just display the title itself as the details
            tvNoteDetails.text = title
        }
    }
}
