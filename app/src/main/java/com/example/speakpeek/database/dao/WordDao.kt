package com.example.speakpeek.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.speakpeek.database.entities.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    fun getAllWords(): List<Word>

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(): List<Word>

    @Insert
    fun insert(word: Word)

    @Delete
    fun delete(word: Word)
}