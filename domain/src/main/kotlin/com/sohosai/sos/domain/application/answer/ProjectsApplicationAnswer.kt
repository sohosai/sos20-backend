package com.sohosai.sos.domain.application.answer

import com.sohosai.sos.domain.application.json.ApplicationItemAnswerJson
import com.sohosai.sos.domain.project.Project

data class ProjectsApplicationAnswer(
    val project: Project,
    val answers: List<ApplicationItemAnswerJson>
)