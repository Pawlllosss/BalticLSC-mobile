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

    override fun onDraw(canvas: Canvas?) {
        var drawPaint = Paint()
        drawPaint.color = Color.BLACK
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 5f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND

        diagram.elements.forEach {
            val x = it.x.toFloat()
            val y = it.y.toFloat()
            canvas?.drawRect(x, y, x + it.width, y + it.height, drawPaint)
            canvas?.drawText(it.name, x, y, drawPaint)
        }

        diagram.lines.forEach {
            it.lineParts.forEach { part ->
                canvas?.drawLine(
                    part.startXY.first.toFloat(),
                    part.startXY.second.toFloat(),
                    part.endXY.first.toFloat(),
                    part.endXY.second.toFloat(),
                    drawPaint
                )
            }
        }
    }
}