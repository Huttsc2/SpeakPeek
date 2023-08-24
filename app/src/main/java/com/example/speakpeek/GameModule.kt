package com.example.speakpeek

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameModule : ComponentActivity() {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var flashcardFlipper: ViewFlipper
    private lateinit var englishWordTextView: TextView
    private lateinit var russianWordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(applicationContext)
        databaseManager.initialize()
        startCheckYourself()
    }

    private fun startCheckYourself() {
        setContentView(R.layout.words)
        initializeFlashcardViews()
        loadRandomWord()

        val exitButton: Button = findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            finish()
        }
    }

    private fun initializeFlashcardViews() {
        flashcardFlipper = findViewById(R.id.flashcardFlipper)
        englishWordTextView = flashcardFlipper.getChildAt(0) as TextView
        russianWordTextView = flashcardFlipper.getChildAt(1) as TextView

        flashcardFlipper.setOnClickListener {
            if (flashcardFlipper.displayedChild == 0) {
                flipCard()
            } else {
                loadRandomWord()
            }
        }
    }

    private fun flipCard() {
        flashcardFlipper.showNext()
    }

    @SuppressLint("SetTextI18n")
    private fun loadRandomWord() {
        val wordDao = databaseManager.db.wordDao()
        CoroutineScope(Dispatchers.IO).launch {
            val randomWords = wordDao.getRandomWord()
            runOnUiThread {
                if (randomWords.isNotEmpty()) {
                    englishWordTextView.text = randomWords[0].englishWord
                    russianWordTextView.text = randomWords[0].translation
                } else {
                    englishWordTextView.text = "error"
                    russianWordTextView.text = "ошибка"
                }
                flashcardFlipper.displayedChild = 0
            }
        }
    }
}