package com.sohosai.sos.domain.application.answer

data class ApplicationItemAnswerLabeledSingleChoice(
    override val itemId: Int,
    // labelId to optionId
    val selectedOptionMap: Map<Int, Int>
) : ApplicationItemAnswer