package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElement
import pl.oczadly.baltic.lsc.diagram.dto.Compartment
import pl.oczadly.baltic.lsc.diagram.dto.drawing.BoxDrawing
import pl.oczadly.baltic.lsc.diagram.dto.drawing.PortDrawing

class DrawableElementConverter(private val shapeConverter: DrawableShapeConverter) {

    fun convertFromBoxDrawing(boxDrawing: BoxDrawing): DrawableElement {
        return DrawableElement(
            getNameFromElementTypeId(boxDrawing.compartments, boxDrawing.elementTypeId) ?: "",
            shapeConverter.convertFromElementTypeId(boxDrawing.elementTypeId),
            boxDrawing.location.x,
            boxDrawing.location.y,
            boxDrawing.location.width,
            boxDrawing.location.height
        )
    }

    fun convertFromPortDrawing(portDrawing: PortDrawing): DrawableElement {
        return DrawableElement(
            getNameFromElementTypeId(portDrawing.compartments, portDrawing.elementTypeId) ?: "",
            shapeConverter.convertFromElementTypeId(portDrawing.elementTypeId),
            portDrawing.location.x,
            portDrawing.location.y,
            portDrawing.location.width,
            portDrawing.location.height
        )
    }

    private fun getNameFromElementTypeId(
        compartments: List<Compartment>,
        elementTypeId: String
    ): String? =
        compartments.filter { compartment -> compartment.compartmentTypeId == "${elementTypeId}Name" }
            .map { compartment -> compartment.value }.firstOrNull()
}
