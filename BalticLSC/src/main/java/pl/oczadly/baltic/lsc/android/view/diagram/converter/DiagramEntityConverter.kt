package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DiagramEntity
import pl.oczadly.baltic.lsc.diagram.dto.Diagram

class DiagramEntityConverter(
    private val elementConverter: DrawableElementConverter,
    private val lineConverter: DrawableLineConverter
) {

    fun convertFromDataStructureDTO(diagram: Diagram): DiagramEntity {
        val drawableBoxes = diagram.boxes.map(elementConverter::convertFromBoxDrawing)
        val drawableElementById = drawableBoxes.map { it.id to it }.toMap()
        val drawablePorts =
            diagram.ports.map { elementConverter.convertFromPortDrawing(it, drawableElementById) }
        val drawableElements = drawableBoxes + drawablePorts
        val drawableLines = diagram.lines.map(lineConverter::convertFromDrawableLine)

        return DiagramEntity(
            diagram.id,
            diagram.name,
            drawableElements,
            drawableLines
        )
    }
}
