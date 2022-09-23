package com.nirmal.service

import com.nirmal.data.models.Post
import com.nirmal.data.repository.post.PostRepository
import com.nirmal.data.request.CreatePostRequest

class PostService(
    private val postRepository: PostRepository
) {
    suspend fun createPostIfUserExists(request: CreatePostRequest): Boolean {
        return postRepository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }
}