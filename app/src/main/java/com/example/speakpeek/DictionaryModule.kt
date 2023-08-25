package com.example.speakpeek

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.example.speakpeek.database.AppDatabase
import com.example.speakpeek.database.entities.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryModule : ComponentActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDictionary()
        initWordsList()
    }

    override fun onResume() {
        super.onResume()
        initWordsList()
    }

    private fun initDictionary() {
        setContentView(R.layout.dictionary)

        val addWord: Button = findViewById(R.id.addWordButton)
        addWord.setOnClickListener {
            startActivity(Intent(this, AddWordModule::class.java))
        }

        val exitButton: Button = findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            finish()
        }
    }

    private fun initWordsList() {
        coroutineScope.launch {
            val words = withContext(Dispatchers.IO) {
                AppDatabase.getInstance(this@DictionaryModule).wordDao().getAllWords().toMutableList()
            }
            val wordsAdapter = WordsAdapter(this@DictionaryModule, words, {})
            wordsAdapter.deleteWordCallback = { word ->
                deleteWord(word, words, wordsAdapter)
            }
            findViewById<ListView>(R.id.words_list).adapter = wordsAdapter
        }
    }


    private fun deleteWord(word: Word, words: MutableList<Word>, wordsAdapter: WordsAdapter) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(this@DictionaryModule).wordDao().delete(word)
            withContext(Dispatchers.Main) {
                words.remove(word)
                wordsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}