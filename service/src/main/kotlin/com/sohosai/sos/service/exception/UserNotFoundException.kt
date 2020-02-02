package com.sohosai.sos.service.exception

import java.util.*

class UserNotFoundException : Exception {
    constructor(userId: UUID) : super("User with userId '$userId' not found (or not enough permission to see).")
    constructor() : super("User not found.")
}