package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.condition.ApplicationConditions
import com.sohosai.sos.domain.application.condition.ApplicationConditionsOp

data class ApplicationConditionsJson(
    val op: ApplicationConditionsOp,
    val conditions: List<ApplicationConditionJson>
) {
    companion object {
        fun fromApplicationConditions(applicationConditions: ApplicationConditions): ApplicationConditionsJson {
            return ApplicationConditionsJson(
                op = applicationConditions.op,
                conditions = applicationConditions.conditions.map { ApplicationConditionJson.fromApplicationCondition(it) }
            )
        }
    }
    fun toApplicationConditions(): ApplicationConditions {
        return ApplicationConditions(
            op = this.op,
            conditions = this.conditions.map { it.toApplicationCondition() }
        )
    }
}