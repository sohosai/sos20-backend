package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.answer.*
import java.util.*

data class ApplicationItemAnswerJson(
    val kind: ApplicationItemKind,
    val itemId: Int,
    val content: String? = null,
    val value: Int? = null,
    val selectedOptionId: Int? = null,
    val selectedOptionMap: Map<Int, Int>? = null,
    val resourceIds: List<String>? = null
) {
    companion object {
        fun fromApplicationItemAnswer(answer: ApplicationItemAnswer): ApplicationItemAnswerJson {
            return when (answer) {
                is ApplicationItemAnswerText -> ApplicationItemAnswerJson(
                    kind = ApplicationItemKind.TEXT,
                    itemId = answer.itemId,
                    content = answer.content
                )
                is ApplicationItemAnswerNumber -> ApplicationItemAnswerJson(
                    kind = ApplicationItemKind.NUMBER,
                    itemId = answer.itemId,
                    value = answer.value
                )
                is ApplicationItemAnswerMultipleChoice -> ApplicationItemAnswerJson(
                    kind = ApplicationItemKind.SINGLE_CHOICE,
                    itemId = answer.itemId,
                    selectedOptionId = answer.selectedOptionId
                )
                is ApplicationItemAnswerSingleChoice -> ApplicationItemAnswerJson(
                    kind = ApplicationItemKind.SINGLE_CHOICE,
                    itemId = answer.itemId,
                    selectedOptionId = answer.selectedOptionId
                )
                is ApplicationItemAnswerLabeledSingleChoice -> ApplicationItemAnswerJson(
                    kind = ApplicationItemKind.MULTIPLE_CHOICE,
                    itemId = answer.itemId,
                    selectedOptionMap = answer.selectedOptionMap
                )
                is ApplicationItemAnswerFile -> ApplicationItemAnswerJson(
                    kind = ApplicationItemKind.FILE,
                    itemId = answer.itemId,
                    resourceIds = answer.resourceIds.map { it.toString() }
                )
                else -> TODO("")
            }
        }
    }
    fun toApplicationItemAnswer(): ApplicationItemAnswer {
        return when (kind) {
            ApplicationItemKind.TEXT -> ApplicationItemAnswerText(
                itemId = itemId,
                content = requireNotNull(content)
            )
            ApplicationItemKind.NUMBER -> ApplicationItemAnswerNumber(
                itemId = itemId,
                value = requireNotNull(value)
            )
            ApplicationItemKind.MULTIPLE_CHOICE -> ApplicationItemAnswerMultipleChoice(
                itemId = itemId,
                selectedOptionId = requireNotNull(selectedOptionId)
            )
            ApplicationItemKind.SINGLE_CHOICE -> ApplicationItemAnswerMultipleChoice(
                itemId = itemId,
                selectedOptionId = requireNotNull(selectedOptionId)
            )
            ApplicationItemKind.LABELED_SINGLE_CHOICE -> ApplicationItemAnswerLabeledSingleChoice(
                itemId = itemId,
                selectedOptionMap = requireNotNull(selectedOptionMap)
            )
            ApplicationItemKind.FILE -> ApplicationItemAnswerFile(
                itemId = itemId,
                resourceIds = requireNotNull(resourceIds).map { UUID.fromString(it) }
            )
        }
    }
}