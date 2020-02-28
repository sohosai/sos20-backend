package com.sohosai.sos.domain.application.item.items

import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.item.ApplicationItemConditions

data class FileApplicationItem(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val conditions: ApplicationItemConditions?,
    override val isRequired: Boolean,
    val allowedTypes: List<String>,
    val isMultipleAllowed: Boolean
) : ApplicationItem