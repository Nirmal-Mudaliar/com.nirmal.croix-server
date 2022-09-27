package com.nirmal.data.repository.user

import com.nirmal.data.models.User
import com.nirmal.data.request.UpdateProfileRequest


interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun updateUser(userId: String, profileImageUrl: String, updateProfileRequest: UpdateProfileRequest): Boolean

    suspend fun doesPasswordForEachUserMatch(email: String, enteredPassword: String): Boolean

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean

    suspend fun searchForUsers(query: String): List<User>


}