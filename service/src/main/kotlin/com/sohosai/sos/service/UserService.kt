package com.sohosai.sos.service

import com.sohosai.sos.domain.project.Project
import com.sohosai.sos.domain.project.ProjectRepository
import com.sohosai.sos.domain.user.*
import com.sohosai.sos.service.exception.NotEnoughPermissionException
import com.sohosai.sos.service.exception.UserNotFoundException
import java.util.*

class UserService(private val userRepository: UserRepository, private val projectRepository: ProjectRepository) {
    suspend fun createUser(
        name: String,
        kanaName: String,
        email: Email,
        phoneNumber: PhoneNumber,
        studentId: String,
        affiliationName: String,
        affiliationType: AffiliationType,
        role: Role,
        authId: String
        ): User {
        userRepository.findUserByAuthId(authId)?.let {
            throw IllegalArgumentException("The user with the auth id already created. See /users/login to get user info.")
        }

        return userRepository.createUser(
            name = name,
            kanaName = kanaName,
            email = email,
            phoneNumber = phoneNumber,
            studentId = studentId,
            affiliationName = affiliationName,
            affiliationType = affiliationType,
            role = role,
            authId = authId
        )
    }

    suspend fun getUserById(userId: UUID, caller: User): User {
        if (!caller.hasPrivilege(Role.COMMITTEE) && caller.id != userId) {
            throw UserNotFoundException(userId)
        }
        return userRepository.findUserById(userId) ?: throw UserNotFoundException(userId)
    }

    suspend fun listUsers(caller: User): List<User> {
        if (!caller.hasPrivilege(Role.COMMITTEE)) {
            throw NotEnoughPermissionException()
        }

        return userRepository.listUsers()
    }

    suspend fun findOwningProject(caller: User): Project? {
        return caller.owningProject()
    }

    suspend fun findSubOwningProject(caller: User): Project? {
        return caller.subOwningProject()
    }

    suspend fun updateUserRole(caller: User, userId: UUID, role: Role) {
        if (!caller.hasPrivilege(Role.ADMIN) || userRepository.findUserById(userId) == null) {
            throw UserNotFoundException()
        }

        userRepository.updateUserRole(userId, role)
    }
}