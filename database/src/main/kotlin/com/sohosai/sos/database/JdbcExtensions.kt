package com.sohosai.sos.database

import kotliquery.Row
import java.util.*

fun Row.uuid(columnIndex: Int): UUID {
    return UUID.nameUUIDFromBytes(bytes(columnIndex))
}