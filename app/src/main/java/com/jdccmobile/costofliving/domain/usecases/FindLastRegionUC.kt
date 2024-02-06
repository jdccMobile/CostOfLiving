package com.jdccmobile.costofliving.domain.usecases

import com.jdccmobile.costofliving.data.RegionRepository

class FindLastRegionUC(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): String = regionRepository.findLastRegion()
}