package pl.oczadly.baltic.lsc.android.view.diagram.entity

data class DrawableLinePart(val startXY: Pair<Int, Int>, val endXY: Pair<Int, Int>) {
    fun getLineDirection(): Direction {
        return when {
            startXY.first < endXY.first -> {
                Direction.RIGHT
            }
            startXY.first > endXY.first -> {
                Direction.LEFT
            }
            startXY.second < endXY.second -> {
                Direction.UP
            }
            else -> {
                Direction.DOWN
            }
        }
    }
}
