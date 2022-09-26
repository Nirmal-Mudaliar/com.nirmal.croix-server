package com.nirmal.service

import com.nirmal.data.models.Activity
import com.nirmal.data.repository.activity.ActivityRepository
import com.nirmal.data.repository.comment.CommentRepository
import com.nirmal.data.repository.post.PostRepository
import com.nirmal.data.utils.ActivityType
import com.nirmal.data.utils.ParentType
import com.nirmal.util.Constants

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) {
    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_ACTIVITY_PAGE_SIZE
    ): List<Activity> {
        return activityRepository.getActivitiesForUser(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ): Boolean {
        val toUserId = when(parentType) {
            is ParentType.Post -> {
                postRepository.getPost(postId = parentId)?.userId

            }
            is ParentType.Comment -> {
                commentRepository.getComment(commentId = parentId)?.userId
            }
            is ParentType.None -> return false
        } ?: return false
        if (byUserId == toUserId) return false
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = toUserId,
                type = when(parentType) {
                    is ParentType.Post -> ActivityType.LikedPost.type
                    is ParentType.Comment -> ActivityType.LikedComment.type
                    else -> ActivityType.LikedPost.type
                },
                parentId = parentId
            )
        )
        return true
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String,
    ): Boolean {
        val userIdOfPOst = postRepository.getPost(postId)?.userId ?: return false
        if (byUserId == userIdOfPOst) return false
//        val toUserId = commentRepository.getComment(commentId = commentId)?.userId ?: return false
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfPOst,
                type = ActivityType.CommentedOnPost.type,
                parentId = postId
            )
        )
        return true

    }

    suspend fun createActivity(activity: Activity) {
        activityRepository.createActivity(activity)
    }

    suspend fun deleteActivity(activityId: String): Boolean {
        return activityRepository.deleteActivity(activityId)
    }
}