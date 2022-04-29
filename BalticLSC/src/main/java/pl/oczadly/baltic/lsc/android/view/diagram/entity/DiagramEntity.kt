package pl.oczadly.baltic.lsc.android.view.diagram.entity

data class DiagramEntity(val id: String, val name: String?, val elements: List<DrawableElement>, val lines: List<DrawableLine>) {
    companion object {
        val emptyDiagram = DiagramEntity("", null, emptyList(), emptyList())
    }
}