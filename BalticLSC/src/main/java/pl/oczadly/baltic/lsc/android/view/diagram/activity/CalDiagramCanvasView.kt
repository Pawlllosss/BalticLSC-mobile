package pl.oczadly.baltic.lsc.android.view.diagram.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DiagramEntity

class CalDiagramCanvasView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var diagram = DiagramEntity.emptyDiagram
    private val shapePaint: Paint = Paint()
    private val linePaint: Paint = Paint()
    private val textPaint: Paint = Paint()

    init {
        shapePaint.color = Color.BLACK
        shapePaint.isAntiAlias = true
        shapePaint.strokeWidth = 3f
        shapePaint.style = Paint.Style.STROKE
        shapePaint.strokeJoin = Paint.Join.ROUND
        shapePaint.strokeCap = Paint.Cap.ROUND

        linePaint.color = Color.BLACK

        textPaint.color = Color.BLUE
        textPaint.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.rotate(90f, width / 2f, height / 2f)
            it.translate(
                -width / 2.5f, height / 4f
            )
            it.scale(1.5f, 1.5f)
        }

        diagram.elements.forEach {
            val x = it.x.toFloat()
            val y = it.y.toFloat()
            canvas?.drawRect(x, y, x + it.width, y + it.height, shapePaint)
            canvas?.drawText(it.name, x, y, textPaint)
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
}