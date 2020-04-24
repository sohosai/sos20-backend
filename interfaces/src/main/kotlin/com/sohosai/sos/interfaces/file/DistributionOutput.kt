package com.sohosai.sos.interfaces.file

import com.sohosai.sos.domain.file.Distribution
import java.time.format.DateTimeFormatter
import java.util.*

data class DistributionOutput(
    val id: UUID,
    val createdAt: String
) {
    companion object {
        fun fromDistribution(distribution: Distribution): DistributionOutput {
            return DistributionOutput(
                id = distribution.id,
                createdAt = distribution.createdAt.format(DateTimeFormatter.ISO_DATE_TIME)
            )
        }
    }
}