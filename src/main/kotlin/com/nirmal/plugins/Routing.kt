package com.nirmal.plugins

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.repository.post.PostRepository
import com.nirmal.data.repository.user.UserRepository
import com.nirmal.routes.*
import com.nirmal.service.FollowService
import com.nirmal.service.PostService
import com.nirmal.service.UserService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userService: UserService by inject()

    val followService: FollowService by inject()

    val postService: PostService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()


    routing {
        // User routes
        createUserRoute(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following
        followUser(followService)
        unfollowUser(followService)

        // Posts
        createPostRoute(postService, userService)


    }
}
