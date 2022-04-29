package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElementShape

class DrawableShapeConverter {

    companion object {
        private val unrecognizedShape = DrawableElementShape("Default")

        private val calShape = DrawableElementShape("CalApp")

        private val shapeByElementTypeId = mapOf(
            "RequiredDataPin" to DrawableElementShape("Input"),
            "ProvidedDataPin" to DrawableElementShape("Output"),
            "ProvidedComputedPin" to DrawableElementShape("PinOutput"),
            "RequiredComputedPin" to DrawableElementShape("PinInput")
        )
    }

    fun convertFromElementTypeId(elementTypeId: String): DrawableElementShape {
        return shapeByElementTypeId[elementTypeId]
            ?: (if (elementTypeId.startsWith("Cal")) calShape else unrecognizedShape)
    }
}
