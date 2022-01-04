package pl.oczadly.baltic.lsc.android.view.app.converter

import pl.oczadly.baltic.lsc.android.view.app.entity.AppResourceRangeEntity
import pl.oczadly.baltic.lsc.app.dto.list.ResourceRange

class AppResourceRangeEntityConverter {

    fun convertFromResourceRangeDTO(resourceRange: ResourceRange): AppResourceRangeEntity =
        AppResourceRangeEntity(
            resourceRange.minCPUs,
            resourceRange.maxCPUs,
            resourceRange.minGPUs,
            resourceRange.maxGPUs,
            resourceRange.minMemory,
            resourceRange.maxMemory,
            resourceRange.minStorage,
            resourceRange.maxStorage
        )

    fun convertToResourceRangeDTO(resourceRange: AppResourceRangeEntity): ResourceRange =
        ResourceRange(
            resourceRange.minCPUs,
            resourceRange.maxCPUs,
            resourceRange.minGPUs,
            resourceRange.maxGPUs,
            resourceRange.minMemory,
            resourceRange.maxMemory,
            resourceRange.minStorage,
            resourceRange.maxStorage
        )
}
