package com.nirmal.data.repository.post

import com.nirmal.data.models.Post
import com.nirmal.util.Constants

interface PostRepository {

    suspend fun createPostIfUserExists(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post>
}