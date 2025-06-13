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
    private val paintLedger = Paint().apply { color = Color.BLACK; strokeWidth = 3f }
    private val stavePositions = mutableListOf<Pair<Float, String>>()
    private var notePosition: Pair<Float, Float>? = null
    private var noteLabel: String? = null

    // Jedyna i poprawna lista nazw nut, która ma 11 pozycji
    private val noteNames = listOf(
        "C", // 0 – dodatkowa linia poniżej (dopisane C)
        "D", // 1 – pod pięciolinią
        "E", // 2 – 1. linia
        "F", // 3 – 1. przestrzeń
        "G", // 4 – 2. linia
        "A", // 5 – 2. przestrzeń
        "B", // 6 – 3. linia
        "c", // 7 – 3. przestrzeń (górna oktawa, małe litery)
        "d", // 8 – 4. linia
        "e", // 9 – 4. przestrzeń
        "f"  // 10 – 5. linia (najwyżej)
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        stavePositions.clear()

        val staveHeight = height * 0.5f
        val lineSpacing = staveHeight / 5f
        val top = height / 4f

        // Pozycje Y wyliczone w tej samej kolejności, co noteNames!
        // 0. C (dopisane) - 2x poniżej 1. linii
        val yC = top + lineSpacing * 6
        stavePositions.add(Pair(yC, "C"))

        // 1. D (pod pięciolinią) - 1x poniżej 1. linii
        val yD = top + lineSpacing * 5
        stavePositions.add(Pair(yD, "D"))

        // 2. E (1. linia)
        val yE = top + lineSpacing * 4
        stavePositions.add(Pair(yE, "E"))
        // 3. F (1. przestrzeń)
        val yF = top + lineSpacing * 3.5f
        stavePositions.add(Pair(yF, "F"))
        // 4. G (2. linia)
        val yG = top + lineSpacing * 3
        stavePositions.add(Pair(yG, "G"))
        // 5. A (2. przestrzeń)
        val yA = top + lineSpacing * 2.5f
        stavePositions.add(Pair(yA, "A"))
        // 6. H (3. linia)
        val yH = top + lineSpacing * 2
        stavePositions.add(Pair(yH, "B"))
        // 7. c (3. przestrzeń)
        val yc = top + lineSpacing * 1.5f
        stavePositions.add(Pair(yc, "c"))
        // 8. d (4. linia)
        val yd = top + lineSpacing * 1
        stavePositions.add(Pair(yd, "d"))
        // 9. e (4. przestrzeń)
        val ye = top + lineSpacing * 0.5f
        stavePositions.add(Pair(ye, "e"))
        // 10. f (5. linia – najwyżej)
        val yf = top
        stavePositions.add(Pair(yf, "f"))

        // Rysowanie pięciolinii (od najwyżej do najniżej)
        for (i in 0 until 5) {
            val y = top + (i * lineSpacing)
            canvas.drawLine(50f, y, width - 50f, y, paintStave)
        }

        // Rysowanie nuty
        notePosition?.let { (x, y) ->
            canvas.drawCircle(x, y, 20f, paintNote)
            if (y == yC) { // jeżeli dopisane C, dorysuj kreskę dodaną
                canvas.drawLine(x - 25f, y, x + 25f, y, paintLedger)
            }
        }
    }

    fun getTouchedNoteName(): String? {
        if (notePosition == null) return null
        val y = notePosition!!.second
        val idx = stavePositions.indexOfFirst { abs(it.first - y) < 1f }
        // idx = index klikniętego miejsca (od 0 do 10)
        return if (idx in noteNames.indices) noteNames[idx] else null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Znajdź najbliższy Y
            val closestPosition = stavePositions.minByOrNull { abs(it.first - event.y) }
            closestPosition?.let {
                notePosition = Pair(event.x, it.first)
                noteLabel = it.second
            }
            invalidate()
        }
        return true
    }
}
