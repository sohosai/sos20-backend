package com.sohosai.sos.domain.file

import java.util.*

interface DistributionRepository {
    suspend fun createDistribution(fileId: UUID, projectId: Int): Distribution
}