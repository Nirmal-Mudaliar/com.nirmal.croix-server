package com.nirmal.routes

import com.nirmal.data.models.Post
import com.nirmal.data.repository.post.PostRepository
import com.nirmal.data.request.CreatePostRequest
import com.nirmal.data.response.BasicApiResponse
import com.nirmal.service.PostService
import com.nirmal.service.UserService
import com.nirmal.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val email = call.principal<JWTPrincipal>()?.getClaim("email", String::class)
            val isEmailByUser = userService.doesEmailBelongToUserId(
                email = email ?: "",
                userId = request.userId
            )
            if (!isEmailByUser) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.UNAUTHORIZED
                    )
                )
                return@post
            }
            val didUserExist = postService.createPostIfUserExists(request)
            if (!didUserExist) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            }

        }
    }
}