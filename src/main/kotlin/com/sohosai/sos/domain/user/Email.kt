package com.sohosai.sos.domain.user

private val VALIDATION_REGEX = Regex("^[a-zA-Z0-9.!#\$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")

data class Email(val value: String) {
    init {
        require(validate(value))
    }

    private fun validate(value: String): Boolean {
        return value.length <= 254 && VALIDATION_REGEX.matches(value)
    }
}