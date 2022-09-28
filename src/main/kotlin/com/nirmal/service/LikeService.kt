package com.nirmal.service

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.repository.likes.LikeRepository
import com.nirmal.data.repository.user.UserRepository
import com.nirmal.data.response.UserResponseItem
import com.nirmal.data.utils.ParentType

class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {
    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.likeParent(userId, parentId, parentType)
    }

    suspend fun unLikeParent(userId: String, parentId: String): Boolean {
        return likeRepository.unLikeParent(userId, parentId)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikesForParent(parentId)
    }

    suspend fun getUsersWhoLikedParent(parentId: String, callerUserId: String): List<UserResponseItem> {
        val userIds = likeRepository.getLikesForParent(parentId).map { it.userId }
        val users = userRepository.getUsersById(userIds)
        val followsByUser = followRepository.getFollowsByUser(callerUserId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )

        }
    }
}