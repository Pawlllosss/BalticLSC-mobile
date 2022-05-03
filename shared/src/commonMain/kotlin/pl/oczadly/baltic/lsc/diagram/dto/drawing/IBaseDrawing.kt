package pl.oczadly.baltic.lsc.diagram.dto.drawing

import pl.oczadly.baltic.lsc.diagram.dto.Compartment

interface IBaseDrawing {
    val id: String
    val elementTypeId: String
    val diagramId: String
    val data: String?
    val type: Int
    val compartments: List<Compartment>
}
