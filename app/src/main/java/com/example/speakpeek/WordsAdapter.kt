package com.example.speakpeek

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.speakpeek.database.entities.Word

class WordsAdapter(
    context: Context,
    words: List<Word>,
    var deleteWordCallback: (Word) -> Unit = {}
) : ArrayAdapter<Word>(context, R.layout.item_word, words) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_word, parent, false)
        val word = getItem(position)!!

        view.findViewById<TextView>(R.id.english_word).text = word.englishWord
        view.findViewById<TextView>(R.id.translation).text = word.translation

        view.findViewById<Button>(R.id.delete_button).setOnClickListener {
            deleteWordCallback(word)
        }

        return view
    }
}
