package com.example.speakpeek

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        initStartMenu()
    }

    private fun initStartMenu() {
        setContentView(R.layout.start_menu)
        val exitButtonInMainMenu: Button = findViewById(R.id.button3)
        exitButtonInMainMenu.setOnClickListener {
            finish()
        }

        val addWord: Button = findViewById(R.id.button2)
        addWord.setOnClickListener {
            initAddWordScreen()
        }

        val startButton: Button = findViewById(R.id.button)
        startButton.setOnClickListener {
            setContentView(R.layout.words)
            initializeFlashcardViews()
            initializeDatabase()
            loadRandomWord()

            val exitButton: Button = findViewById(R.id.exitButton)
            exitButton.setOnClickListener {
                initStartMenu()
            }
        }
    }

    private fun initAddWordScreen() {
        setContentView(R.layout.add_word)

        initializeDatabase()

        val englishWordInput: EditText = findViewById(R.id.englishWordInput)
        val translationInput: EditText = findViewById(R.id.translationInput)

        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val englishWord = englishWordInput.text.toString()
            val translation = translationInput.text.toString()

            if (englishWord.isNotBlank() && translation.isNotBlank()) {
                addWordToDatabase(englishWord, translation)
                englishWordInput.setText("")
                translationInput.setText("")
            } else {
                Toast.makeText(this, "Пожалуйста, заполните оба поля!", Toast.LENGTH_SHORT).show()
            }
        }

        val exitButton: Button = findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            initStartMenu()
        }
    }

    private fun addWordToDatabase(englishWord: String, translation: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val wordDao = db.wordDao()
            wordDao.insert(Word(null, englishWord, translation))
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

    @SuppressLint("SetTextI18n")
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