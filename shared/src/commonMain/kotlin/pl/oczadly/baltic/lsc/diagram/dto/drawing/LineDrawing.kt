package pl.oczadly.baltic.lsc.diagram.dto.drawing

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.diagram.dto.Compartment

@Serializable
data class LineDrawing(
    override val id: String,
    override val elementTypeId: String,
    override val diagramId: String,
    override val data: String?,
    override val type: Int,
    override val compartments: List<Compartment>,
    val startElement: String,
    val endElement: String,
    val points: List<Int> // masterpiece, each pair is a (x,y) coordinate
) : IBaseDrawing
