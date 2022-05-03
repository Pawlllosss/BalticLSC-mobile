package pl.oczadly.baltic.lsc.android.view.diagram.converter

import android.graphics.Color
import android.graphics.Paint
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElementShape

class DrawableShapeConverter {

    companion object {
        private val calShapePaint = {
            val paint = Paint()
            paint.color = Color.GRAY
            paint.isAntiAlias = true
            paint.strokeWidth = 3f
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint
        }

        private val providedPin = {
            val paint = Paint()
            paint.color = Color.GREEN
            paint.isAntiAlias = true
            paint.strokeWidth = 2f
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint
        }

        private val requiredPin = {
            val paint = Paint()
            paint.color = Color.RED
            paint.isAntiAlias = true
            paint.strokeWidth = 2f
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint
        }

        private val computedPin = {
            val paint = Paint()
            paint.color = Color.BLACK
            paint.isAntiAlias = true
            paint.strokeWidth = 2f
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint
        }

        private val unrecognizedShape = DrawableElementShape("Default", Paint())

        private val calShape = DrawableElementShape("CalApp", calShapePaint())

        private val shapeByElementTypeId = mapOf(
            "RequiredDataPin" to DrawableElementShape("Input", providedPin()),
            "ProvidedDataPin" to DrawableElementShape("Output", requiredPin()),
            "ProvidedComputedPin" to DrawableElementShape("PinOutput", computedPin()),
            "RequiredComputedPin" to DrawableElementShape("PinInput", computedPin())
        )
    }

    fun convertFromElementTypeId(elementTypeId: String): DrawableElementShape {
        return shapeByElementTypeId[elementTypeId]
            ?: (if (elementTypeId.startsWith("Cal")) calShape else unrecognizedShape)
    }
}
