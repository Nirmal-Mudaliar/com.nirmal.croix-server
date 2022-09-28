package com.nirmal.routes

import com.google.gson.Gson
import com.nirmal.data.request.CreatePostRequest
import com.nirmal.data.request.DeletePostRequest
import com.nirmal.data.response.BasicApiResponse
import com.nirmal.service.CommentService
import com.nirmal.service.LikeService
import com.nirmal.service.PostService
import com.nirmal.service.UserService
import com.nirmal.util.ApiResponseMessages
import com.nirmal.util.Constants
import com.nirmal.util.QueryParams
import com.nirmal.util.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*

fun Route.createPost(
    postService: PostService,
) {
    val gson: Gson by inject()
    authenticate {

        post("/api/post/create") {

            val multipart = call.receiveMultipart()
            var createPostRequest: CreatePostRequest? = null
            var fileName: String? = null
            multipart.forEachPart { partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "post_data") {
                            createPostRequest = gson.fromJson(partData.value, CreatePostRequest::class.java)
                        }
                    }
                    is PartData.FileItem -> {
                        fileName = partData.save(Constants.POST_PICTURE_PATH)

                    }
                    is PartData.BinaryItem -> Unit
                    is PartData.BinaryChannelItem -> Unit
                }


            }

            val postPictureUrl = "${Constants.BASE_URL}post_pictures/$fileName"
            createPostRequest?.let {request ->
                val createPostAcknowledged = postService.createPost(
                    request = request,
                    userId = call.userId,
                    imageUrl = postPictureUrl
                )
                if (createPostAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true
                        )
                    )
                } else {
                    File("${Constants.POST_PICTURE_PATH}/$fileName").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }


        }
    }
}

fun Route.getPostByFollows(
    postService: PostService,
) {
    authenticate {
        get("/api/post/get") {

            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            val posts = postService.getPostByFollows(
                userId = call.userId,
                page = page,
                pageSize = pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}


fun Route.deletePost(
    postService: PostService,
    commentService: CommentService,
    likeService: LikeService,
) {
    authenticate {
        delete("/api/post/delete") {
            val request = call.receiveOrNull<DeletePostRequest>() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@delete
            }
            val post = postService.getPost(request.postId)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            if (post.userId == call.userId) {
                postService.deletePost(postId = request.postId)
                likeService.deleteLikesForParent(request.postId)
                commentService.deleteCommentsforPost(request.postId)
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(successful = true)
                )
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }

        }
    }

}




























