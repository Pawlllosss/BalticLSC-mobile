package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableElement
import pl.oczadly.baltic.lsc.diagram.dto.drawing.BoxDrawing
import pl.oczadly.baltic.lsc.diagram.dto.drawing.PortDrawing

class DrawableElementConverter {

    fun convertFromBoxDrawing(boxDrawing: BoxDrawing): DrawableElement {
        return DrawableElement(
            "",
            boxDrawing.elementTypeId,
            boxDrawing.location.x,
            boxDrawing.location.y,
            boxDrawing.location.width,
            boxDrawing.location.height
        )
    }

    fun convertFromPortDrawing(portDrawing: PortDrawing): DrawableElement {
        return DrawableElement(
            "",
            portDrawing.elementTypeId,
            portDrawing.location.x,
            portDrawing.location.y,
            portDrawing.location.width,
            portDrawing.location.height
        )
    }
}
