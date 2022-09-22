package com.nirmal.data.repository.follow

interface FollowRepository {
    suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

    suspend fun unFollowUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean
}