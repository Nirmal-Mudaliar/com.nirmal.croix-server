package com.nirmal.service

import com.nirmal.data.models.Comment
import com.nirmal.data.repository.comment.CommentRepository
import com.nirmal.data.request.CreateCommentRequest
import com.nirmal.util.Constants

class CommentService(
    private val commentRepository: CommentRepository
) {
    suspend fun createComment(request: CreateCommentRequest): ValidationEvent {
        request.apply {
            if (comment.isBlank() || userId.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorEmptyField
            }
            if (comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }
        commentRepository.createComment(
            Comment(
                comment = request.comment,
                userId = request.userId,
                postId = request.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun getCommentsForPost(postId: String): List<Comment> {
        return commentRepository.getCommentsForPost(postId)
    }

    sealed class ValidationEvent {
        object ErrorEmptyField: ValidationEvent()
        object ErrorCommentTooLong: ValidationEvent()
        object Success: ValidationEvent()
    }
}