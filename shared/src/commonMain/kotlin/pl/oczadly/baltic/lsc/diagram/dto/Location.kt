package pl.oczadly.baltic.lsc.diagram.dto

import kotlinx.serialization.Serializable

@Serializable
data class Location(val height: Int, val width: Int, val x: Int, val y: Int)
