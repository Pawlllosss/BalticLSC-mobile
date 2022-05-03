package pl.oczadly.baltic.lsc.android.view.diagram.entity

data class DrawableElement(
    val id: String,
    val name: String,
    val shape: DrawableElementShape,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
)
