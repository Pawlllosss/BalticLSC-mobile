package pl.oczadly.baltic.lsc.android.view.diagram.entity

// TODO: consider converting DrawableTypeId to some object representing type info (shape etc.)
data class DrawableElement(val name: String, val drawableTypeId: String, val x: Int, val y: Int, val width: Int, val height: Int)
