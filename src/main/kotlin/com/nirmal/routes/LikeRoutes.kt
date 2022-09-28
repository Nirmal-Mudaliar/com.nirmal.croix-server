package com.nirmal.routes

import com.nirmal.data.request.LikeUpdateRequest
import com.nirmal.data.response.BasicApiResponse
import com.nirmal.data.utils.ParentType
import com.nirmal.service.ActivityService
import com.nirmal.service.LikeService
import com.nirmal.util.ApiResponseMessages
import com.nirmal.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeParent(
    likeService: LikeService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/like") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val likeSuccessful = likeService.likeParent(call.userId, request.parentId, request.parentType)
            if (likeSuccessful) {
                activityService.addLikeActivity(
                    byUserId = call.userId,
                    parentType = ParentType.fromType(request.parentType),
                    parentId = request.parentId

                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unLikeParent(
    likeService: LikeService,
) {
    authenticate {
        delete("/api/unlike") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val unLikeSuccessful = likeService.unLikeParent(call.userId, request.parentId)
            if (unLikeSuccessful) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }

        }
    }
}

fun Route.getLikesForParent(likeService: LikeService) {
    authenticate {
        get("/api/like/parent") {
            val parentId = call.parameters[QueryParams.PARAM_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val usersWhoLikedParent = likeService.getUsersWhoLikedParent(parentId = parentId, callerUserId = call.userId)
            call.respond(
                HttpStatusCode.OK,
                usersWhoLikedParent
            )
        }
    }
}

