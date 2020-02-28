package com.sohosai.sos.domain.application.item.items

import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.item.ApplicationItemConditions
import com.sohosai.sos.domain.application.item.ApplicationItemOption

data class SingleChoiceApplicationItem(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val conditions: ApplicationItemConditions?,
    override val isRequired: Boolean,
    val options: List<ApplicationItemOption>
) : ApplicationItem