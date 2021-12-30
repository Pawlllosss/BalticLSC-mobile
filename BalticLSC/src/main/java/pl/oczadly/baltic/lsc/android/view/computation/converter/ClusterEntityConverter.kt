package pl.oczadly.baltic.lsc.android.view.computation.converter

import pl.oczadly.baltic.lsc.android.view.computation.entity.ClusterEntity
import pl.oczadly.baltic.lsc.computation.dto.Cluster

class ClusterEntityConverter {

    fun convertFromClusterDTO(cluster: Cluster): ClusterEntity =
        ClusterEntity(cluster.uid, cluster.name)
}
