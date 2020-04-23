package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionAnswered
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionAnsweredItem
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionAttributes
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionCategory
import com.sohosai.sos.domain.application.item.ApplicationItemCondition
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory

data class ApplicationConditionJson(
    val kind: ApplicationConditionKind,
    val applicationId: Int? = null,
    val shouldBeSelected: Boolean? = null,
    val itemCondition: ApplicationItemCondition? = null,
    val category: ProjectCategory? = null,
    val attributes: List<ProjectAttribute>? = null
) {
    companion object {
        fun fromApplicationCondition(condition: ApplicationCondition): ApplicationConditionJson {
            return when (condition) {
                is ApplicationConditionAnswered -> ApplicationConditionJson(
                    kind = ApplicationConditionKind.ANSWERED_APPLICATION,
                    applicationId = condition.applicationId,
                    shouldBeSelected = condition.shouldBeSelected
                )
                is ApplicationConditionAnsweredItem -> ApplicationConditionJson(
                    kind = ApplicationConditionKind.ANSWERED_APPLICATION_ITEM,
                    applicationId = condition.applicationId,
                    itemCondition = condition.itemCondition
                )
                is ApplicationConditionCategory -> ApplicationConditionJson(
                    kind = ApplicationConditionKind.PROJECT_CATEGORY,
                    category = condition.category
                )
                is ApplicationConditionAttributes -> ApplicationConditionJson(
                    kind = ApplicationConditionKind.PROJECT_ATTRIBUTES,
                    attributes = condition.attributes
                )
                else -> TODO("")
            }
        }
    }
    fun toApplicationCondition(): ApplicationCondition {
        return when (kind) {
            ApplicationConditionKind.ANSWERED_APPLICATION -> ApplicationConditionAnswered(
                applicationId = requireNotNull(applicationId),
                shouldBeSelected = requireNotNull(shouldBeSelected)
            )
            ApplicationConditionKind.ANSWERED_APPLICATION_ITEM -> ApplicationConditionAnsweredItem(
                applicationId = requireNotNull(applicationId),
                itemCondition = requireNotNull(itemCondition)
            )
            ApplicationConditionKind.PROJECT_CATEGORY -> ApplicationConditionCategory(
                category = requireNotNull(category)
            )
            ApplicationConditionKind.PROJECT_ATTRIBUTES -> ApplicationConditionAttributes(
                attributes = requireNotNull(attributes)
            )
        }
    }
}