package com.example.atmusic

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class StaveView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paintStave = Paint().apply { color = Color.BLACK; strokeWidth = 5f }
    private val paintNote = Paint().apply { color = Color.BLACK; style = Paint.Style.FILL }
    private val paintLedger = Paint().apply { color = Color.BLACK; strokeWidth = 3f } // Kreska dla dodatkowych linii
    private val stavePositions = mutableListOf<Pair<Float, String>>() // Pozycje linii i przestrzeni z identyfikatorami
    private var notePosition: Pair<Float, Float>? = null
    private var noteLabel: String? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val staveHeight = height * 0.5f
        val lineSpacing = staveHeight / 5f
        stavePositions.clear()

        // Rysowanie pięciolinii + zapis indeksów linii i przestrzeni
        for (i in 0 until 5) {
            val y = (height / 4f) + (i * lineSpacing)
            stavePositions.add(Pair(y, "Linia $i"))
            canvas.drawLine(50f, y, width - 50f, y, paintStave)
        }

        // Dodanie przestrzeni między liniami (dla nut na przestrzeni)
        for (i in 0 until 4) {
            val y = (stavePositions[i].first + stavePositions[i + 1].first) / 2
            stavePositions.add(Pair(y, "Przestrzeń $i"))
        }

        // 🔹 **Dodanie miejsca na nutę D pod ostatnią linią**
        val belowLastLine = stavePositions.last().first + lineSpacing
        stavePositions.add(Pair(belowLastLine, "Pod ostatnią linią (D)"))

        // 🔹 **Dodanie miejsca na dopisaną linię dla C**
        val extraLedgerLine = belowLastLine + lineSpacing
        stavePositions.add(Pair(extraLedgerLine, "Dopisane C"))

        // Rysowanie nuty w "przyciągniętym" miejscu
        notePosition?.let { (x, y) ->
            canvas.drawCircle(x, y, 20f, paintNote)
            if (y == extraLedgerLine) {
                canvas.drawLine(x - 25f, y, x + 25f, y, paintLedger) // 🔹 Dodana kreska dla C
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val closestPosition = stavePositions.minByOrNull { abs(it.first - event.y) }
            closestPosition?.let {
                notePosition = Pair(event.x, it.first)
                noteLabel = it.second // Przypisanie identyfikatora nuty
            }
            invalidate() // Odśwież widok
        }
        return true
    }
}
