package com.sohosai.sos.domain.user

private val VALIDATION_REGEX = Regex("\\A0[0-9]{9,10}\\z")

data class PhoneNumber(val value: String) {
    init {
        require(validate(value))
    }

    private fun validate(value: String): Boolean = VALIDATION_REGEX.matches(value)
}