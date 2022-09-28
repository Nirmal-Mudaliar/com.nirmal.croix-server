package com.nirmal.service

import com.nirmal.data.models.Post
import com.nirmal.data.repository.post.PostRepository
import com.nirmal.data.request.CreatePostRequest
import com.nirmal.util.Constants

class PostService(
    private val postRepository: PostRepository
) {
    suspend fun createPost(request: CreatePostRequest, userId: String, imageUrl: String): Boolean {
        return postRepository.createPost(
            Post(
                imageUrl = imageUrl,
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

    suspend fun getPostByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return postRepository.getPostByFollows(userId, page, pageSize)
    }

    suspend fun getPost(postId: String): Post? {
        return postRepository.getPost(postId)
    }

    suspend fun getPostForProfile(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return postRepository.getPostForProfile(userId, page, pageSize)
    }

    suspend fun deletePost(postId: String) {
        postRepository.deletePost(postId)
    }
}