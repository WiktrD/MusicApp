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

    // Note names ordered bottom to top; must match the order of stavePositions (11 entries)
    private val noteNames = listOf(
        "C", // 0 – ledger line below the stave
        "D", // 1 – below the stave
        "E", // 2 – 1st line
        "F", // 3 – 1st space
        "G", // 4 – 2nd line
        "A", // 5 – 2nd space
        "B", // 6 – 3rd line
        "c", // 7 – 3rd space (upper octave, lowercase)
        "d", // 8 – 4th line
        "e", // 9 – 4th space
        "f"  // 10 – 5th line (top)
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        stavePositions.clear()

        val staveHeight = height * 0.5f
        val lineSpacing = staveHeight / 5f
        val top = height / 4f

        // Y positions, computed in the same order as noteNames
        // 0. C (ledger line) - 2 spacings below the 1st line
        val yC = top + lineSpacing * 6
        stavePositions.add(Pair(yC, "C"))

        // 1. D (below the stave) - 1 spacing below the 1st line
        val yD = top + lineSpacing * 5
        stavePositions.add(Pair(yD, "D"))

        // 2. E (1st line)
        val yE = top + lineSpacing * 4
        stavePositions.add(Pair(yE, "E"))
        // 3. F (1st space)
        val yF = top + lineSpacing * 3.5f
        stavePositions.add(Pair(yF, "F"))
        // 4. G (2nd line)
        val yG = top + lineSpacing * 3
        stavePositions.add(Pair(yG, "G"))
        // 5. A (2nd space)
        val yA = top + lineSpacing * 2.5f
        stavePositions.add(Pair(yA, "A"))
        // 6. B (3rd line)
        val yB = top + lineSpacing * 2
        stavePositions.add(Pair(yB, "B"))
        // 7. c (3rd space)
        val yc = top + lineSpacing * 1.5f
        stavePositions.add(Pair(yc, "c"))
        // 8. d (4th line)
        val yd = top + lineSpacing * 1
        stavePositions.add(Pair(yd, "d"))
        // 9. e (4th space)
        val ye = top + lineSpacing * 0.5f
        stavePositions.add(Pair(ye, "e"))
        // 10. f (5th line, top)
        val yf = top
        stavePositions.add(Pair(yf, "f"))

        // Draw the stave (top line first)
        for (i in 0 until 5) {
            val y = top + (i * lineSpacing)
            canvas.drawLine(50f, y, width - 50f, y, paintStave)
        }

        // Draw the note
        notePosition?.let { (x, y) ->
            canvas.drawCircle(x, y, 20f, paintNote)
            if (y == yC) { // the ledger C needs an extra short line through the note
                canvas.drawLine(x - 25f, y, x + 25f, y, paintLedger)
            }
        }
    }

    /** Name of the note the player has placed on the stave, or null if none yet. */
    fun getSelectedNoteName(): String? {
        if (notePosition == null) return null
        val y = notePosition!!.second
        val idx = stavePositions.indexOfFirst { abs(it.first - y) < 1f }
        // idx = index of the selected position (0..10)
        return if (idx in noteNames.indices) noteNames[idx] else null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Snap to the closest line/space
            val closestPosition = stavePositions.minByOrNull { abs(it.first - event.y) }
            closestPosition?.let {
                notePosition = Pair(event.x, it.first)
            }
            invalidate()
        }
        return true
    }
}
