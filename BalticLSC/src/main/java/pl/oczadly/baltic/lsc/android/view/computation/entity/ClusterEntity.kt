package pl.oczadly.baltic.lsc.android.view.computation.entity

data class ClusterEntity(
    val uid: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
