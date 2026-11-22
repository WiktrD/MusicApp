package com.example.musicproject.fragmentsModels.Game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.musicproject.R
import com.example.musicproject.fragmentsModels.MainMenu.MainMenuFragment
import com.example.musicproject.managers.AnimationManager

class GameResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fanfareAnim = view.findViewById<LottieAnimationView>(R.id.lottie_fanfare)
        AnimationManager.showLottieAnimation(fanfareAnim)

        // Read the score passed in the arguments
        val score = arguments?.getInt("score", 0) ?: 0

        val scoreText = view.findViewById<TextView>(R.id.score_value)
        scoreText.text = score.toString()

        // "Play again" button: back to the game selection screen
        view.findViewById<Button>(R.id.button_play_again).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GameFragment())
                .addToBackStack(null)
                .commit()
        }

        // "Back to menu" button
        view.findViewById<Button>(R.id.button_backToMenu).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainMenuFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
