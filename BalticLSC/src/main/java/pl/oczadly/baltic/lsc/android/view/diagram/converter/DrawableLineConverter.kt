package pl.oczadly.baltic.lsc.android.view.diagram.converter

import pl.oczadly.baltic.lsc.android.view.diagram.entity.DrawableLine
import pl.oczadly.baltic.lsc.diagram.dto.drawing.LineDrawing

class DrawableLineConverter {

    fun convertFromDrawableLine(drawableLine: LineDrawing): DrawableLine {
        // TODO: need to take 2 pairs of coordinates (window of 4) but procees only by 2
        val xyCoordinates = drawableLine.points.chunked(2).map { (x, y) -> Pair(x, y) }
        return DrawableLine(xyCoordinates)
    }
}
