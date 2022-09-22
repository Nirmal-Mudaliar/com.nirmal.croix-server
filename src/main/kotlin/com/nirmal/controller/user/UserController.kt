package com.nirmal.controller.user

import com.nirmal.data.models.User


interface UserController {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?
}