package com.example.atmusic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PianoView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val whiteKeys = mutableListOf<Rect>()
    private val blackKeys = mutableListOf<Rect>()
    private var pressedKeyIndex: Int? = null

    private val whitePaint = Paint().apply { color = Color.WHITE; style = Paint.Style.FILL }
    private val blackPaint = Paint().apply { color = Color.BLACK; style = Paint.Style.FILL }
    private val pressedPaint = Paint().apply { color = Color.LTGRAY; style = Paint.Style.FILL }
    private val borderPaint = Paint().apply { color = Color.DKGRAY; style = Paint.Style.STROKE; strokeWidth = 4f }

    private var onKeyPressedListener: ((Int) -> Unit)? = null
    fun setOnKeyPressedListener(listener: (Int) -> Unit) {
        this.onKeyPressedListener = listener
    }

    private var onKeyReleasedListener: ((Int) -> Unit)? = null
    fun setOnKeyReleasedListener(listener: (Int) -> Unit) {
        onKeyReleasedListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val keyWidth = width / 8f
        val keyHeight = height * 0.5f

        whiteKeys.clear()
        blackKeys.clear()

        // White keys: C, D, E, F, G, A, B, C
        for (i in 0 until 8) {
            val rect = Rect((i * keyWidth).toInt(), 50, ((i + 1) * keyWidth).toInt(), (50 + keyHeight).toInt())
            whiteKeys.add(rect)
            canvas.drawRect(rect, if (pressedKeyIndex == i) pressedPaint else whitePaint)
            canvas.drawRect(rect, borderPaint)
        }

        // Black keys: C#, D#, F#, G#, A#
        val blackKeyHeight = keyHeight * 0.6f
        val blackKeyIndexes = listOf(0, 1, 3, 4, 5)
        val blackKeyOffset = keyWidth * 0.75f

        for ((i, index) in blackKeyIndexes.withIndex()) {
            val rect = Rect(
                (index * keyWidth + blackKeyOffset).toInt(),
                50,
                (index * keyWidth + keyWidth * 1.5f).toInt(),
                (50 + blackKeyHeight).toInt()
            )
            blackKeys.add(rect)
            canvas.drawRect(rect, if (pressedKeyIndex == BLACK_KEY_ID_OFFSET + i) pressedPaint else blackPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Check black keys first (they are drawn on top of white ones)
                blackKeys.forEachIndexed { index, rect ->
                    if (rect.contains(x, y)) {
                        pressedKeyIndex = BLACK_KEY_ID_OFFSET + index
                        invalidate()
                        onKeyPressedListener?.invoke(mapBlackKeyToMidi(index))
                        return true
                    }
                }

                // Then white keys
                whiteKeys.forEachIndexed { index, rect ->
                    if (rect.contains(x, y)) {
                        pressedKeyIndex = index
                        invalidate()
                        onKeyPressedListener?.invoke(mapWhiteKeyToMidi(index))
                        return true
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                pressedKeyIndex?.let { index ->
                    val midiNote = if (index >= BLACK_KEY_ID_OFFSET) {
                        mapBlackKeyToMidi(index - BLACK_KEY_ID_OFFSET)
                    } else {
                        mapWhiteKeyToMidi(index)
                    }

                    postDelayed({
                        onKeyReleasedListener?.invoke(midiNote)
                    }, KEY_RELEASE_DELAY_MS)
                }

                pressedKeyIndex = null
                invalidate()
            }
        }

        return true
    }

    // Mapping of keys to MIDI notes
    private fun mapWhiteKeyToMidi(index: Int): Int {
        val whiteNotes = listOf(60, 62, 64, 65, 67, 69, 71, 72) // C4 -> C5
        return whiteNotes.getOrElse(index) { 60 }
    }

    private fun mapBlackKeyToMidi(index: Int): Int {
        val blackNotes = listOf(61, 63, 66, 68, 70) // C#4, D#4, F#4, G#4, A#4
        return blackNotes.getOrElse(index) { 61 }
    }

    private companion object {
        // Black keys use ids starting here in pressedKeyIndex (white keys use 0..7)
        const val BLACK_KEY_ID_OFFSET = 100

        // The note is released this long after the finger is lifted
        const val KEY_RELEASE_DELAY_MS = 200L
    }
}
