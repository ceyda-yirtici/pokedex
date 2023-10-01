package com.example.obss_first_day

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var random = 0 // Initialize random number
    var remainingAttempts = 5 // Total attempts allowed
    lateinit var textInput: EditText
    lateinit var button: Button
    lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textInput = findViewById(R.id.editTextInput)
        button = findViewById(R.id.button)
        result = findViewById(R.id.textView)

        startNewGame()

        button.setOnClickListener {
            handleButtonClick()
        }
    }

    private fun startNewGame() {
        random = Random.nextInt(1, 101)
        remainingAttempts = 5
        result.text = "Kalan hakkın: $remainingAttempts"
        button.text = "Tahmin Et"
        button.isEnabled = true
        textInput.text.clear()
    }

    private fun handleButtonClick() {
        val guessedNumber = textInput.text.toString().toIntOrNull()
        hideKeyboard()
        if (guessedNumber != null) {
            if (guessedNumber == random) {
                result.text = "Tebrikler kazandın!"
                button.text = "Tekrar Oyna"
                button.setOnClickListener {
                    recreate()
                }
            } else {
                remainingAttempts--
                if (remainingAttempts > 0) {
                    if (guessedNumber < random) {
                        result.text = "Daha büyük bir sayı girin. Kalan hakkın: $remainingAttempts"
                    } else {
                        result.text = "Daha küçük bir sayı girin. Kalan hakkın: $remainingAttempts"
                    }
                } else {
                    result.text = "Kaybettin, tekrar dene! Doğru sayı: $random"
                    button.text = "Tekrar Oyna"
                    button.setOnClickListener {
                        recreate()
                    }

                }
            }
            textInput.text.clear()

        }

    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
