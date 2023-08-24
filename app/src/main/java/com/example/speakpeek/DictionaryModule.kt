package com.example.speakpeek

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.speakpeek.database.entities.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DictionaryModule : ComponentActivity() {

    private lateinit var databaseManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseManager = DatabaseManager(applicationContext)
        databaseManager.initialize()
        initDictionary()
    }

    private fun initDictionary() {
        setContentView(R.layout.add_word)

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
            finish()
        }
    }

    private fun addWordToDatabase(englishWord: String, translation: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val wordDao = databaseManager.db.wordDao()
            wordDao.insert(Word(null, englishWord, translation))
        }
    }
}