package com.sohosai.sos.database

import kotliquery.Row
import java.util.*

fun Row.uuid(columnLabel: String): UUID {
    return UUID.fromString(string(columnLabel))
}

fun Row.uuidOrNull(columnLabel: String): UUID? {
    return stringOrNull(columnLabel)?.let { UUID.fromString(it) }
}