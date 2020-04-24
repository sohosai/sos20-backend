package com.sohosai.sos.domain.file

import java.util.*

data class StoredFile(
    val id: UUID,
    val name: String
) {
    fun nameWithoutExtension() = name.split(".").getOrNull(0) ?: ""
    fun extension() = name.split(".").let {
        if (it.size > 1) it.last()
        else ""
    }
}