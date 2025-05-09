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
    private val whitePaint = Paint().apply { color = Color.WHITE; style = Paint.Style.FILL }
    private val blackPaint = Paint().apply { color = Color.BLACK; style = Paint.Style.FILL }
    private val pressedPaint = Paint().apply { color = Color.LTGRAY; style = Paint.Style.FILL }
    private val borderPaint = Paint().apply { color = Color.DKGRAY; style = Paint.Style.STROKE; strokeWidth = 4f }
    private var pressedKey: Rect? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val keyWidth = width / 8f
        val keyHeight = height * 0.5f // 🔹 Skrócona wysokość klawiszy!

        whiteKeys.clear()
        blackKeys.clear()

        // Rysowanie białych klawiszy (C, D, E, F, G, A, B, C)
        for (i in 0 until 8) {
            val rect = Rect((i * keyWidth).toInt(), 50, ((i + 1) * keyWidth).toInt(), (50 + keyHeight).toInt())
            whiteKeys.add(rect)
            canvas.drawRect(rect, if (pressedKey == rect) pressedPaint else whitePaint)
            canvas.drawRect(rect, borderPaint) // Dodanie krawędzi
        }

        // Rysowanie czarnych klawiszy (C#, D#, F#, G#, A#)
        val blackKeyHeight = keyHeight * 0.6f
        val blackKeyIndexes = listOf(0, 1, 3, 4, 5)
        val blackKeyOffset = keyWidth * 0.75f

        for (i in blackKeyIndexes) {
            val rect = Rect((i * keyWidth + blackKeyOffset).toInt(), 50, (i * keyWidth + keyWidth * 1.5f).toInt(), (50 + blackKeyHeight).toInt())
            blackKeys.add(rect)
            canvas.drawRect(rect, if (pressedKey == rect) pressedPaint else blackPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            pressedKey = blackKeys.find { it.contains(event.x.toInt(), event.y.toInt()) }
                ?: whiteKeys.find { it.contains(event.x.toInt(), event.y.toInt()) }
            invalidate()
        } else if (event.action == MotionEvent.ACTION_UP) {
            pressedKey = null
            invalidate()
        }
        return true
    }
}
