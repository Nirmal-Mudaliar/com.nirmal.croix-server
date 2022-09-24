package com.nirmal.service

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.request.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.followUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }

    suspend fun unFollowUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.unFollowUserIfExists(
            followingUserId,
            request.followedUserId
        )
    }
}