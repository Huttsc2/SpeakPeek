package com.example.speakpeek.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val englishWord: String,
    val translation: String
)