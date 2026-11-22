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
import com.example.musicproject.managers.ScreenNavigator

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

        val score = arguments?.getInt(ARG_SCORE, 0) ?: 0

        val scoreText = view.findViewById<TextView>(R.id.score_value)
        scoreText.text = score.toString()

        // "Play again" button: back to the game selection screen
        view.findViewById<Button>(R.id.button_play_again).setOnClickListener {
            ScreenNavigator.open(parentFragmentManager, GameFragment())
        }

        // "Back to menu" button
        view.findViewById<Button>(R.id.button_backToMenu).setOnClickListener {
            ScreenNavigator.open(parentFragmentManager, MainMenuFragment())
        }
    }

    companion object {
        private const val ARG_SCORE = "score"

        @JvmStatic
        fun newInstance(score: Int): GameResultFragment = GameResultFragment().apply {
            arguments = Bundle().apply { putInt(ARG_SCORE, score) }
        }
    }
}
