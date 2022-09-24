package com.nirmal.routes

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.request.FollowUpdateRequest
import com.nirmal.data.response.BasicApiResponse
import com.nirmal.service.FollowService
import com.nirmal.service.UserService
import com.nirmal.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(
    followService: FollowService,
    userService: UserService
) {
    authenticate {
        post("/api/following/follow") {
            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            ifEmailBelongsToUser(
                userId = request.followingUserId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                if (followService.followUserIfExists(request)) {
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

}

fun Route.unfollowUser(followService: FollowService) {
    delete("/api/following/unfollow") {

        val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        val didUserExists = followService.unFollowUserIfExists(request)
        if (didUserExists) {
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