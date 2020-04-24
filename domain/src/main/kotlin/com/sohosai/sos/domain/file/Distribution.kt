package com.sohosai.sos.domain.file

import java.time.LocalDateTime
import java.util.*

data class Distribution(
    val id: UUID,
    val fileId: UUID,
    val projectId: Int,
    val createdAt: LocalDateTime
)