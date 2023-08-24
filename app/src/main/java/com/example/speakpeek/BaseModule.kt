package com.example.speakpeek

import android.content.Context
import com.example.speakpeek.database.AppDatabase
import com.example.speakpeek.database.entities.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseManager(context: Context) {
    val db: AppDatabase = AppDatabase.getInstance(context)

    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            if (db.wordDao().getAllWords().isEmpty()) {
                val wordDao = db.wordDao()
                wordDao.insert(Word(null, "hello", "привет"))
                wordDao.insert(Word(null, "world", "мир"))
            }
        }
    }
}