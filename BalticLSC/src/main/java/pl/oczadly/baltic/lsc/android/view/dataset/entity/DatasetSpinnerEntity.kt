package pl.oczadly.baltic.lsc.android.view.dataset.entity

data class DatasetSpinnerEntity(
    val pinUid: String,
    val datesetUid: String,
    val datasetName: String,
) {
    override fun toString(): String {
        return datasetName
    }
}
