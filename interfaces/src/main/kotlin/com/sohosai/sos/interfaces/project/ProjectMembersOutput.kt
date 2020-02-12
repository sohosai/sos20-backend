package com.sohosai.sos.interfaces.project

import com.sohosai.sos.domain.project.ProjectMembers
import com.sohosai.sos.interfaces.user.UserOutput

data class ProjectMembersOutput(
    val owner: UserOutput,
    val subOwner: UserOutput?
) {
    companion object {
        fun fromProjectMembers(projectMembers: ProjectMembers): ProjectMembersOutput {
            return ProjectMembersOutput(
                owner = UserOutput.fromUser(projectMembers.owner),
                subOwner = projectMembers.subOwner?.let { UserOutput.fromUser(it) }
            )
        }
    }
}