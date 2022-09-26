package com.nirmal.service

import com.nirmal.data.repository.likes.LikeRepository
import com.nirmal.data.utils.ParentType

class LikeService(
    private val likeRepository: LikeRepository
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
}