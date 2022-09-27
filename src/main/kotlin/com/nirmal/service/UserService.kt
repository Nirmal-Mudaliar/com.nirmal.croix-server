package com.nirmal.service

import com.nirmal.data.models.User
import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.repository.user.UserRepository
import com.nirmal.data.request.CreateAccountRequest
import com.nirmal.data.response.UserResponseItem

class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    suspend fun doesUserWithEmailExist(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun createUser(request: CreateAccountRequest ) {
        userRepository.createUser(
            user = User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                githubUrl = null,
                linkedinUrl = null,
                instagramUrl = null
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent {
        if (request.email.isBlank() || request.username.isBlank() || request.password.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.Success
    }

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean {
        return userRepository.doesEmailBelongToUserId(email, userId)
    }

    suspend fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    suspend fun searchForUsers(query: String, userId: String): List<UserResponseItem> {
        val users = userRepository.searchForUsers(query)
        val followsByUser = followRepository.getFollowsByUser(userId = userId)
        return users.map { user ->
            val isFollowing: Boolean = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }

    sealed class ValidationEvent() {
        object Success: ValidationEvent()
        object ErrorFieldEmpty: ValidationEvent()
    }
}