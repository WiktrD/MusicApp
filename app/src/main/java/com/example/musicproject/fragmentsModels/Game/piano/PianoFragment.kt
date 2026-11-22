package com.example.musicproject.fragmentsModels.Game.piano

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.atmusic.PianoView
import com.example.musicproject.R
import com.example.musicproject.fragmentsModels.Game.GameResultFragment
import com.example.musicproject.managers.AnimationManager

class PianoFragment : Fragment() {

    private lateinit var viewModel: PianoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_piano, container, false)
        val pianoView = view.findViewById<PianoView>(R.id.piano_view)
        viewModel = ViewModelProvider(this)[PianoViewModel::class.java]
        pianoView.setOnKeyReleasedListener { midiNote ->
            viewModel.stopNote(midiNote)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val introAnim = view.findViewById<LottieAnimationView>(R.id.lottie_intro)
        AnimationManager.showLottieAnimation(introAnim)

        viewModel.startGameTimer(GAME_DURATION_MS)

        val timeText = view.findViewById<TextView>(R.id.time_text)
        viewModel.timeLeft.observe(viewLifecycleOwner) { time ->
            timeText.text = "$time s"
        }

        viewModel.isGameEnded().observe(viewLifecycleOwner) { ended ->
            if (ended) {
                saveScoreAndShowResult()
            }
        }

        val correctText = view.findViewById<TextView>(R.id.correct_note)
        val incorrectText = view.findViewById<TextView>(R.id.wrong_note)
        val noteText = view.findViewById<TextView>(R.id.random_note)
        val pianoView = view.findViewById<PianoView>(R.id.piano_view)
        val successAnim = view.findViewById<LottieAnimationView>(R.id.lottie_success)
        val lossAnim = view.findViewById<LottieAnimationView>(R.id.lottie_loss)

        viewModel.targetNoteLive.observe(viewLifecycleOwner) { midi ->
            val noteName = midiToNoteName(midi)
            noteText.text = getString(R.string.play_note_prompt, noteName)
        }

        viewModel.correctCountLive.observe(viewLifecycleOwner) { correct ->
            correctText.text = "$correct"
        }

        viewModel.incorrectCountLive.observe(viewLifecycleOwner) { incorrect ->
            incorrectText.text = "$incorrect"
        }

        pianoView.setOnKeyPressedListener { midiNote ->
            val beforeCorrect = viewModel.correctCount
            val beforeIncorrect = viewModel.incorrectCount

            viewModel.checkNote(midiNote)
            viewModel.playNote(midiNote)

            val afterCorrect = viewModel.correctCount
            val afterIncorrect = viewModel.incorrectCount

            if (afterCorrect > beforeCorrect) {
                AnimationManager.showLottieAnimation(successAnim)
            } else if (afterIncorrect > beforeIncorrect) {
                AnimationManager.showLottieAnimation(lossAnim)
            }
        }
    }

    private fun saveScoreAndShowResult() {
        val score = viewModel.finishGame()
        val bundle = Bundle().apply { putInt("score", score) }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GameResultFragment().apply { arguments = bundle })
            .addToBackStack(null)
            .commit()
    }

    private fun midiToNoteName(midi: Int): String {
        val noteNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val note = noteNames[midi % 12]
        val octave = midi / 12 - 1
        return "$note$octave"
    }

    private companion object {
        const val GAME_DURATION_MS = 60_000L
    }
}
