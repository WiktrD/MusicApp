package com.example.musicproject.fragmentsModels.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musicproject.R
import com.example.musicproject.fragmentsModels.MainMenu.MainMenuFragment
import com.example.musicproject.fragmentsModels.Game.piano.PianoFragment

class GameResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pobierz wynik z Bundla
        val score = arguments?.getInt("score", 0) ?: 0

        // Wyświetl wynik
        val resultText = view.findViewById<TextView>(R.id.quiz_result)
        resultText.text = score.toString()

        // Obsługa przycisku "Zagraj ponownie"
        view.findViewById<Button>(R.id.quiz_playAgain).setOnClickListener {
            // Wróć do PianoFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GameFragment())
                .addToBackStack(null)
                .commit()
        }

        // Obsługa przycisku "Wróć do menu"
        view.findViewById<Button>(R.id.button_backToMenu).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainMenuFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
