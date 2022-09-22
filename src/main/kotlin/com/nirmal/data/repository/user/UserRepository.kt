package com.nirmal.data.repository.user

import com.nirmal.data.models.User


interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun doesPasswordForEachUserMatch(email: String, enteredPassword: String): Boolean
}