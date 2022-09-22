package com.nirmal.data.request

data class FollowUpdateRequest(
    val followingUserId: String,
    val followedUserId: String,
)
