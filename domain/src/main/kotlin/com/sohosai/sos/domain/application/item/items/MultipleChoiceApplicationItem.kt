package com.sohosai.sos.domain.application.item.items

import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.item.ApplicationItemConditions
import com.sohosai.sos.domain.application.item.ApplicationItemOption
import com.sohosai.sos.domain.application.item.ApplicationItemOptionLabel

data class MultipleChoiceApplicationItem(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val conditions: ApplicationItemConditions?,
    val labels: List<ApplicationItemOptionLabel>,
    val options: List<ApplicationItemOption>
) : ApplicationItem