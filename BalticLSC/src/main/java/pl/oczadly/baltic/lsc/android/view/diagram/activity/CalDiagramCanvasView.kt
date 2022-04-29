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

    // TODO: add diagram property and iterate over it in onDraw https://stackoverflow.com/questions/9233800/how-to-get-current-canvas

    override fun onDraw(canvas: Canvas?) {
        var drawPaint = Paint()
        drawPaint.color = Color.BLACK
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 5f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND

        diagram.elements.forEach {
            canvas?.drawCircle(it.x.toFloat(), it.y.toFloat(), it.width.toFloat(), drawPaint)
        }

//        canvas?.drawCircle(50f, 50f, 20f, drawPaint)
//        drawPaint.color = Color.GREEN
//        canvas?.drawCircle(50f, 150f, 20f, drawPaint)
//        drawPaint.color = Color.BLUE
//        canvas?.drawCircle(50f, 250f, 20f, drawPaint)
    }
}