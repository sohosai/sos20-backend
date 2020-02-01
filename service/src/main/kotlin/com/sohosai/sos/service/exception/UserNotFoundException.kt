package com.sohosai.sos.service.exception

import java.util.*

class UserNotFoundException : Exception {
    constructor(userId: UUID) : super("User with userId '$userId' not found.")
    constructor(authId: String) : super("User with authId '$authId' not found.")
}