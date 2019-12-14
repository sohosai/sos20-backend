package com.sohosai.sos.infrastructure.repository.jdbc

import kotliquery.Row
import java.util.*

fun Row.uuid(columnIndex: Int): UUID {
    return UUID.nameUUIDFromBytes(bytes(columnIndex))
}