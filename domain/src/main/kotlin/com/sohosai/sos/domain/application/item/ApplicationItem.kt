package com.sohosai.sos.domain.application.item

interface ApplicationItem {
    val id: Int
    val name: String
    val description: String
    val conditions: ApplicationItemConditions?
}