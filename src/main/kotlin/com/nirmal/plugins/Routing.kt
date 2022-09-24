package com.nirmal.plugins

import com.nirmal.routes.*
import com.nirmal.service.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()

    val followService: FollowService by inject()

    val postService: PostService by inject()

    val likeService: LikeService by inject()

    val commentService: CommentService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()


    routing {
        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following
        followUser(followService, userService)
        unfollowUser(followService)

        // Posts
        createPost(postService, userService)
        getPostByFollows(postService, userService)
        deletePost(postService, likeService, userService)

        // likes
        likeParent(likeService = likeService, userService = userService)
        unLikeParent(likeService = likeService, userService = userService)

        // Comments
        createComment(commentService, userService)
        deleteComment(commentService, likeService, userService)
        getCommentForPost(commentService)


    }
}
