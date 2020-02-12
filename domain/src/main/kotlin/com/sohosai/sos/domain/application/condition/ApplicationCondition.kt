package com.sohosai.sos.domain.application.condition

import com.sohosai.sos.domain.project.Project

interface ApplicationCondition {
    fun isMatchForProject(project: Project)
}