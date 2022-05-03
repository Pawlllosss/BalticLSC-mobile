package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableLine
import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableLinePart
import pl.oczadly.baltic.lsc.diagram.dto.drawing.LineDrawing

class DrawableLineConverter {

    fun convertFromDrawableLine(drawableLine: LineDrawing): DrawableLine {
        val points = drawableLine.points
        val lineParts = mutableListOf<DrawableLinePart>()

        for (i in 0..points.size - 4 step 2) {
            val xyStartCoordinates = points[i] to points[i + 1]
            val xyEndCoordinates = points[i + 2] to points[i + 3]
            lineParts.add(DrawableLinePart(xyStartCoordinates, xyEndCoordinates))
        }

        return DrawableLine(lineParts)
    }
}
