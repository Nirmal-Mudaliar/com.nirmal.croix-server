package com.nirmal.data.repository.likes

import com.nirmal.data.models.Like
import com.nirmal.data.utils.ParentType
import com.nirmal.util.Constants

interface LikeRepository {
    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean
    suspend fun unLikeParent(userId: String, parentId: String): Boolean
    suspend fun deleteLikesForParent(parentId: String)
    suspend fun getLikesForParent(parentId: String, page: Int = 0, pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE): List<Like>
}