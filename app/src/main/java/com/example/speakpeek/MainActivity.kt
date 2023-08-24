package com.example.speakpeek

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStartMenu()
    }

    private fun initStartMenu() {
        setContentView(R.layout.main_menu)

        val startButton: Button = findViewById(R.id.StartButtonMainMenu)
        startButton.setOnClickListener {
            startActivity(Intent(this, GameModule::class.java))
        }

        val addWord: Button = findViewById(R.id.DictionaryButtonMainMenu)
        addWord.setOnClickListener {
            startActivity(Intent(this, DictionaryModule::class.java))
        }

        val exitButtonInMainMenu: Button = findViewById(R.id.ExitButtonMainMenu)
        exitButtonInMainMenu.setOnClickListener {
            finish()
        }
    }
}