package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.condition.ApplicationCondition
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionAnswered
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionAnsweredItem
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionAttributes
import com.sohosai.sos.domain.application.condition.conditions.ApplicationConditionCategory
import com.sohosai.sos.domain.application.item.ApplicationItemConditions
import com.sohosai.sos.domain.project.ProjectAttribute
import com.sohosai.sos.domain.project.ProjectCategory

data class ApplicationConditionJson(
    val kind: ApplicationConditionKind,
    val applicationId: Int? = null,
    val itemConditions: ApplicationItemConditions? = null,
    val category: ProjectCategory? = null,
    val attributes: List<ProjectAttribute>? = null
) {
    companion object {
        fun fromApplicationCondition(condition: ApplicationCondition): ApplicationConditionJson {
            return when (condition) {
                is ApplicationConditionAnswered -> ApplicationConditionJson(
                    kind = ApplicationConditionKind.ANSWERED_APPLICATION,
                    applicationId = condition.applicationId
                )
                is ApplicationConditionAnsweredItem -> ApplicationConditionJson(
                    kind = ApplicationConditionKind.ANSWERED_APPLICATION_ITEM,
                    applicationId = condition.applicationId,
                    itemConditions = condition.itemConditions
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
                applicationId = requireNotNull(applicationId)
            )
            ApplicationConditionKind.ANSWERED_APPLICATION_ITEM -> ApplicationConditionAnsweredItem(
                applicationId = requireNotNull(applicationId),
                itemConditions = requireNotNull(itemConditions)
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