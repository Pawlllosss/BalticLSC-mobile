package pl.oczadly.baltic.lsc.diagram.dto.drawing

import kotlinx.serialization.Serializable
import pl.oczadly.baltic.lsc.diagram.dto.Compartment
import pl.oczadly.baltic.lsc.diagram.dto.Location

@Serializable
data class PortDrawing(
    override val id: String,
    override val elementTypeId: String,
    override val diagramId: String,
    override val data: String?,
    override val type: Int,
    override val compartments: List<Compartment>,
    var parentId: String,
    val location: Location
) : IBaseDrawing
