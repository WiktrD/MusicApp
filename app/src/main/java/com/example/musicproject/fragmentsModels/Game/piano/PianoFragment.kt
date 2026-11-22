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
import com.example.musicproject.AppViewModelFactory
import com.example.musicproject.R
import com.example.musicproject.fragmentsModels.Game.GameResultFragment
import com.example.musicproject.managers.AnimationManager
import com.example.musicproject.managers.ScreenNavigator

class PianoFragment : Fragment() {

    private lateinit var viewModel: PianoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_piano, container, false)
        val pianoView = view.findViewById<PianoView>(R.id.piano_view)
        viewModel = ViewModelProvider(this, AppViewModelFactory(requireContext()))[PianoViewModel::class.java]
        pianoView.setOnKeyReleasedListener { midiNote ->
            viewModel.stopNote(midiNote)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val introAnim = view.findViewById<LottieAnimationView>(R.id.lottie_intro)
        AnimationManager.showLottieAnimation(introAnim)

        val timeText = view.findViewById<TextView>(R.id.time_text)
        val correctText = view.findViewById<TextView>(R.id.correct_note)
        val incorrectText = view.findViewById<TextView>(R.id.wrong_note)
        val noteText = view.findViewById<TextView>(R.id.random_note)
        val pianoView = view.findViewById<PianoView>(R.id.piano_view)
        val successAnim = view.findViewById<LottieAnimationView>(R.id.lottie_success)
        val lossAnim = view.findViewById<LottieAnimationView>(R.id.lottie_loss)

        viewModel.timeLeft.observe(viewLifecycleOwner) { time ->
            timeText.text = "$time s"
        }

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

        viewModel.answerResult.observe(viewLifecycleOwner) { event ->
            event.contentIfNotHandled?.let { isCorrect ->
                AnimationManager.showLottieAnimation(if (isCorrect) successAnim else lossAnim)
            }
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) { event ->
            event.contentIfNotHandled?.let { score ->
                ScreenNavigator.open(parentFragmentManager, GameResultFragment.newInstance(score))
            }
        }

        pianoView.setOnKeyPressedListener { midiNote ->
            viewModel.checkNote(midiNote)
            viewModel.playNote(midiNote)
        }
    }

    private fun midiToNoteName(midi: Int): String {
        val noteNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val note = noteNames[midi % 12]
        val octave = midi / 12 - 1
        return "$note$octave"
    }
}
