package com.nirmal.service

import com.nirmal.data.models.User
import com.nirmal.data.repository.user.UserRepository
import com.nirmal.data.request.CreateAccountRequest
import com.nirmal.data.request.LoginRequest
import com.nirmal.data.response.BasicApiResponse
import com.nirmal.util.ApiResponseMessages
import io.ktor.server.application.*
import io.ktor.server.response.*

class UserService(
    private val userRepository: UserRepository
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

    suspend fun doesPasswordForEachUserMatch(request: LoginRequest): Boolean {
        return userRepository.doesPasswordForEachUserMatch(
            request.email,
            request.password
        )

    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    sealed class ValidationEvent() {
        object Success: ValidationEvent()
        object ErrorFieldEmpty: ValidationEvent()
    }
}