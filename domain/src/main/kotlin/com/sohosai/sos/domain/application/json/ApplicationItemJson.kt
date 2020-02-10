package com.sohosai.sos.domain.application.json

import com.sohosai.sos.domain.application.item.ApplicationItem
import com.sohosai.sos.domain.application.item.ApplicationItemConditions
import com.sohosai.sos.domain.application.item.ApplicationItemOption
import com.sohosai.sos.domain.application.item.ApplicationItemOptionLabel
import com.sohosai.sos.domain.application.item.items.*

data class ApplicationItemJson(
    val kind: ApplicationItemKind,
    val id: Int,
    val name: String,
    val description: String,
    val conditions: ApplicationItemConditions?,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val placeHolder: String? = null,
    val min: Int? = null,
    val max: Int? = null,
    val options: List<ApplicationItemOption>? = null,
    val labels: List<ApplicationItemOptionLabel>? = null,
    val allowedType: List<String>? = null,
    val isMultipleAllowed: Boolean? = null
) {
    companion object {
        fun fromApplicationItem(item: ApplicationItem): ApplicationItemJson {
            return when (item) {
                is TextApplicationItem -> ApplicationItemJson(
                    kind = ApplicationItemKind.TEXT,
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    conditions = item.conditions,
                    minLength = item.minLength,
                    maxLength = item.maxLength,
                    placeHolder = item.placeHolder
                )
                is NumberApplicationItem -> ApplicationItemJson(
                    kind = ApplicationItemKind.NUMBER,
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    conditions = item.conditions,
                    min = item.min,
                    max = item.max,
                    placeHolder = item.placeHolder
                )
                is SingleChoiceApplicationItem -> ApplicationItemJson(
                    kind = ApplicationItemKind.SINGLE_CHOICE,
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    conditions = item.conditions,
                    options = item.options
                )
                is MultipleChoiceApplicationItem -> ApplicationItemJson(
                    kind = ApplicationItemKind.MULTIPLE_CHOICE,
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    conditions = item.conditions,
                    labels = item.labels,
                    options = item.options
                )
                is FileApplicationItem -> ApplicationItemJson(
                    kind = ApplicationItemKind.FILE,
                    id = item.id,
                    name = item.name,
                    description = item.description,
                    conditions = item.conditions,
                    allowedType = item.allowedType,
                    isMultipleAllowed = item.isMultipleAllowed
                )
                else -> TODO("")
            }
        }
    }
    fun toApplicationItem(): ApplicationItem {
        return when(kind) {
            ApplicationItemKind.TEXT -> TextApplicationItem(
                id = id,
                name = name,
                description = description,
                conditions = conditions,
                minLength = requireNotNull(minLength),
                maxLength = requireNotNull(maxLength),
                placeHolder = placeHolder
            )
            ApplicationItemKind.NUMBER -> NumberApplicationItem(
                id = id,
                name = name,
                description = description,
                conditions = conditions,
                min = requireNotNull(min),
                max = requireNotNull(max),
                placeHolder = placeHolder
            )
            ApplicationItemKind.SINGLE_CHOICE -> SingleChoiceApplicationItem(
                id = id,
                name = name,
                description = description,
                conditions = conditions,
                options = requireNotNull(options)
            )
            ApplicationItemKind.MULTIPLE_CHOICE -> MultipleChoiceApplicationItem(
                id = id,
                name = name,
                description = description,
                conditions = conditions,
                labels = requireNotNull(labels),
                options = requireNotNull(options)
            )
            ApplicationItemKind.FILE -> FileApplicationItem(
                id = id,
                name = name,
                description = description,
                conditions = conditions,
                allowedType = requireNotNull(allowedType),
                isMultipleAllowed = requireNotNull(isMultipleAllowed)
            )
        }
    }
}