package com.sohosai.sos.domain.application.item.items

import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.item.ApplicationItemConditions

data class NumberApplicationItem(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val conditions: ApplicationItemConditions?,
    override val isRequired: Boolean,
    val min: Int,
    val max: Int,
    val placeHolder: String?,
    val unit: String?
) : ApplicationItem