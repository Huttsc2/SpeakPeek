package com.example.speakpeek

import android.os.Bundle
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.ComponentActivity
import com.example.speakpeek.database.AppDatabase
import com.example.speakpeek.database.entities.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var flashcardFlipper: ViewFlipper
    private lateinit var englishWordTextView: TextView
    private lateinit var russianWordTextView: TextView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

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
        initializeDatabase()
        loadRandomWord()
    }

    private fun initializeDatabase() {
        db = AppDatabase.getInstance(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            if (db.wordDao().getAllWords().isEmpty()) {
                val wordDao = db.wordDao()
                wordDao.insert(Word(null, "hello", "привет"))
                wordDao.insert(Word(null, "world", "мир"))
            }
        }
    }

    private fun loadRandomWord() {
        val wordDao = db.wordDao()
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

    private fun flipCard() {
        flashcardFlipper.showNext()
    }
}