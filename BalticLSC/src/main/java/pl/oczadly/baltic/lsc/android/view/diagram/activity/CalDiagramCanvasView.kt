package pl.oczadly.baltic.lsc.android.view.diagram.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DiagramEntity
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElement

class CalDiagramCanvasView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var diagram = DiagramEntity.emptyDiagram
    private val linePaint: Paint = Paint()
    private val textPaint: Paint = Paint()

    companion object {
        private const val textSize = 18f
    }

    init {
        linePaint.color = Color.BLACK

        textPaint.color = Color.BLUE
        textPaint.textSize = textSize
        textPaint.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.rotate(90f, width / 2f, height / 2f)
            it.translate(
                -width / 2.7f, height / 4f
            )
            it.scale(1.5f, 1.5f)
        }

        diagram.elements.forEach {
            val x = it.x.toFloat()
            val y = it.y.toFloat()
            canvas?.drawRect(x, y, x + it.width, y + it.height, it.shape.paint)
            val nameX = calculateNameXPosition(it)
            canvas?.drawText(it.name, nameX, y - 5, textPaint)
        }

        diagram.lines.forEach {
            it.lineParts.forEach { part ->
                canvas?.drawLine(
                    part.startXY.first.toFloat(),
                    part.startXY.second.toFloat(),
                    part.endXY.first.toFloat(),
                    part.endXY.second.toFloat(),
                    linePaint
                )
            }
        }
    }

    private fun calculateNameXPosition(element: DrawableElement) =
        element.x + element.width / 2 - element.name.length / 2 * textSize / 2
}