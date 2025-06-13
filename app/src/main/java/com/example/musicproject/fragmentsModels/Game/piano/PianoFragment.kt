package com.example.musicproject.fragmentsModels.Game.piano

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.atmusic.PianoView
import com.example.musicproject.R
import com.example.musicproject.database.AppDatabase
import com.example.musicproject.database.Wynik
import com.example.musicproject.fragmentsModels.Game.GameResultFragment
import kotlinx.coroutines.launch



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
        pianoView.viewModel = viewModel
        pianoView.setOnKeyPressedListener { note ->
            viewModel.playNote(note)
        }
        pianoView.setOnKeyReleasedListener { midiNote ->
            viewModel.stopNote(midiNote)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // dodano dla timera – uruchomienie gry
        viewModel.startGameTimer(60_000) // 60 sekund


        // dodano dla timera – obserwowanie czasu
        val timeText = view.findViewById<TextView>(R.id.time_text) // musisz mieć TextView np. u góry
        viewModel.timeLeft.observe(viewLifecycleOwner) { time ->
            timeText.text = "$time s"
        }


        // dodano dla timera – zakończenie gry
        viewModel.isGameEnded().observe(viewLifecycleOwner) { ended ->
            if (ended) {
                val correct = viewModel.correctCount
                val incorrect = viewModel.incorrectCount
                val score = correct - incorrect

                // dodano zapis do bazy
                val wynik = Wynik(
                    typGry = "pianino",
                    punkty = score,
                    poprawne = correct,
                    bledne = incorrect
                )

                val db = AppDatabase.getInstance(requireContext())
                lifecycleScope.launch {
                    db.wynikDao().insertWynik(wynik)

                    // po zapisie przejdź do ekranu wyników
                    val bundle = Bundle().apply {
                        putInt("score", score)
                    }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, GameResultFragment().apply { arguments = bundle })
                        .addToBackStack(null)
                        .commit()
                }
            }
        }




        // Wszystkie operacje na widoku tutaj!
        val correctText = view.findViewById<TextView>(R.id.correct_note)
        val incorrectText = view.findViewById<TextView>(R.id.wrong_note)
        val noteText = view.findViewById<TextView>(R.id.random_note)
        val pianoView = view.findViewById<PianoView>(R.id.piano_view)

        viewModel = ViewModelProvider(this)[PianoViewModel::class.java]
        pianoView.viewModel = viewModel

        viewModel.targetNoteLive.observe(viewLifecycleOwner) { midi ->
            noteText.text = "Zagraj nutę: ${midiToNoteName(midi)}"
        }


        viewModel.correctCountLive.observe(viewLifecycleOwner) { correct ->
            correctText.text = "$correct"
        }

        viewModel.incorrectCountLive.observe(viewLifecycleOwner) { incorrect ->
            incorrectText.text = "$incorrect"
        }

        pianoView.setOnKeyPressedListener { midiNote ->
            viewModel.checkNote(midiNote)
            viewModel.playNote(midiNote)
        }
    }

    private fun updateScoreText(scoreText: TextView) {
        val correct = viewModel.correctCountLive.value ?: 0
        val incorrect = viewModel.incorrectCountLive.value ?: 0
    }

    private fun midiToNoteName(midi: Int): String {
        val noteNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val note = noteNames[midi % 12]
        val octave = midi / 12 - 1
        return "$note$octave"
    }
}
