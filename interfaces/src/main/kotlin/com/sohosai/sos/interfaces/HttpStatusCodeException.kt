package com.sohosai.sos.interfaces

class HttpStatusCodeException(val code: Int, override val message: String?) : Exception(message) {
}