package com.nirmal.data.repository.comment

import com.nirmal.data.models.Comment
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CommentRepositoryImpl(
    db: CoroutineDatabase
): CommentRepository {

    private val comments = db.getCollection<Comment>()

    override suspend fun createComment(comment: Comment) {
        comments.insertOne(comment)
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deletedCount = comments.deleteOneById(commentId).deletedCount
        return deletedCount > 0
    }

    override suspend fun deleteCommentsFromPost(postId: String): Boolean {
        val deletedCount = comments.deleteMany(Comment::postId eq postId).deletedCount
        return deletedCount > 0
    }

    override suspend fun getCommentsForPost(postId: String): List<Comment> {
        return comments.find(Comment::postId eq postId).toList()
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }
}