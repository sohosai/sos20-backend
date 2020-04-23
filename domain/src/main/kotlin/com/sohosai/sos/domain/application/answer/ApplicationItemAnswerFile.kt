package com.sohosai.sos.domain.application.answer

import java.util.*

data class ApplicationItemAnswerFile(
    override val itemId: Int,
    val fileIds: List<UUID>
) : ApplicationItemAnswer