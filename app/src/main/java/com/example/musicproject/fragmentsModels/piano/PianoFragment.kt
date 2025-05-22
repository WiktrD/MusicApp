package com.example.musicproject.fragmentsModels.piano

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.atmusic.PianoView
import com.example.musicproject.R


class PianoFragment : Fragment() {

    private lateinit var viewModel: PianoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_piano, container, false)
        val pianoView = view.findViewById<PianoView>(R.id.piano_view)
        viewModel = ViewModelProvider(this)[PianoViewModel::class.java]
        pianoView.viewModel = viewModel
        pianoView.setOnKeyPressedListener { note ->
            viewModel.playNote(note)
        }
        return view
    }
}