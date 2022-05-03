package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElement
import pl.oczadly.baltic.lsc.diagram.dto.Compartment
import pl.oczadly.baltic.lsc.diagram.dto.drawing.BoxDrawing
import pl.oczadly.baltic.lsc.diagram.dto.drawing.PortDrawing

class DrawableElementConverter(private val shapeConverter: DrawableShapeConverter) {

    fun convertFromBoxDrawing(boxDrawing: BoxDrawing): DrawableElement {
        return DrawableElement(
            boxDrawing.id,
            getNameFromElementTypeId(boxDrawing.compartments, boxDrawing.elementTypeId) ?: "",
            shapeConverter.convertFromElementTypeId(boxDrawing.elementTypeId),
            boxDrawing.location.x,
            boxDrawing.location.y,
            boxDrawing.location.width,
            boxDrawing.location.height
        )
    }

    fun convertFromPortDrawing(
        portDrawing: PortDrawing,
        drawableElementById: Map<String, DrawableElement>
    ): DrawableElement {
        val parentElement = drawableElementById[portDrawing.parentId]
        val parentX = parentElement?.x ?: 0
        val parentY = parentElement?.y ?: 0

        return DrawableElement(
            portDrawing.id,
            getPortName(portDrawing.compartments, portDrawing.elementTypeId) ?: "",
            shapeConverter.convertFromElementTypeId(portDrawing.elementTypeId),
            portDrawing.location.x + parentX,
            portDrawing.location.y + parentY,
            30,
            30
        )
    }

    private fun getNameFromElementTypeId(
        compartments: List<Compartment>,
        elementTypeId: String
    ): String? =
        findCompartmentValueByTypeId(
            compartments,
            if (elementTypeId.startsWith("Cal")) "${elementTypeId}CUName" else "${elementTypeId}Name"
        )

    private fun findCompartmentValueByTypeId(
        compartments: List<Compartment>,
        typeId: String
    ) =
        compartments.filter { compartment -> compartment.compartmentTypeId == typeId }
            .map { compartment -> compartment.value }.firstOrNull()

    private fun getPortName(compartments: List<Compartment>, elementTypeId: String): String? {
        return when (elementTypeId) {
            "RequiredComputedPin" -> findCompartmentValueByTypeId(compartments, "RequiredPortName")
            "ProvidedComputedPin" -> findCompartmentValueByTypeId(compartments, "ProvidedPortName")
            else -> null
        }
    }
}
