package com.nirmal.service

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.request.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest): Boolean {
        return followRepository.followUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }

    suspend fun unFollowUserIfExists(request: FollowUpdateRequest): Boolean {
        return followRepository.unFollowUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }
}