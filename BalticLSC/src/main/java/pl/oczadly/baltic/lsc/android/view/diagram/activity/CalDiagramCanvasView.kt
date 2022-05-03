package pl.oczadly.baltic.lsc.android.view.diagram.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DiagramEntity
import pl.oczadly.baltic.lsc.android.view.diagram.entity.Direction
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElement
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableLinePart

class CalDiagramCanvasView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var diagram = DiagramEntity.emptyDiagram
    private val linePaint: Paint = Paint()
    private val textPaint: Paint = Paint()

    companion object {
        private const val textSize = 18f
        private const val arrowWidth = 10f
        private const val arrowHeight = 20f
    }

    init {
        linePaint.color = Color.BLACK
        linePaint.strokeWidth = 2f

        textPaint.color = Color.BLUE
        textPaint.textSize = textSize
        textPaint.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas?) {
        val maxX = diagram.elements.map(DrawableElement::x).map(Int::toFloat).maxOrNull()
            ?: width.toFloat()
        val maxY = diagram.elements.map(DrawableElement::y).map(Int::toFloat).maxOrNull()
            ?: height.toFloat()

        canvas?.let {
            it.rotate(90f, width / 2f, height / 2f)
            it.translate(-height / 5f, width / 3f)
            val scaleX = height / maxX
            val scaleY = width / maxY
            val scale = if (scaleX < scaleY) scaleX else scaleY

            it.scale(scale * 0.87f, scale * 0.87f)
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

            val lastPart = it.lineParts.last()
            drawArrow(lastPart, canvas)
        }
    }

    private fun calculateNameXPosition(element: DrawableElement) =
        element.x + element.width / 2 - element.name.length / 2 * textSize / 2

    private fun drawArrow(
        lastPart: DrawableLinePart,
        canvas: Canvas?
    ) {
        val direction = lastPart.getLineDirection()
        val endXY = lastPart.endXY

        val path = Path()

        val x = endXY.first.toFloat()
        val y = endXY.second.toFloat()

        path.moveTo(x, y)
        if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            val xOffset = if (direction == Direction.RIGHT) -arrowWidth else arrowWidth
            val yOffset = arrowHeight / 2f
            path.lineTo(x + xOffset, y + yOffset)
            path.lineTo(x + xOffset, y - yOffset)
        } else {
            val xOffset = arrowHeight / 2f
            val yOffset = if (direction == Direction.UP) -arrowWidth else arrowWidth
            path.lineTo(x + xOffset, y + yOffset)
            path.lineTo(x - xOffset, y + yOffset)
        }

        path.close();

        canvas?.drawPath(path, linePaint)
    }
}