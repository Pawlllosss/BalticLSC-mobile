package pl.oczadly.baltic.lsc.diagram.dto

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.diagram.dto.drawing.BoxDrawing
import pl.oczadly.baltic.lsc.diagram.dto.drawing.LineDrawing
import pl.oczadly.baltic.lsc.diagram.dto.drawing.PortDrawing

@Serializable
data class Diagram(
    val id: String,
    val name: String?,
    val data: String?,
    val boxes: List<BoxDrawing>,
    val lines: List<LineDrawing>,
    val ports: List<PortDrawing>
)
