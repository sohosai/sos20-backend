package com.sohosai.sos.domain.file

import java.util.*

interface DistributionRepository {
    suspend fun createDistribution(fileId: UUID, projectId: Int): Distribution
    suspend fun findDistributionById(id: UUID): Distribution?
    suspend fun findDistributionsForProject(projectName: Int): List<Distribution>
}