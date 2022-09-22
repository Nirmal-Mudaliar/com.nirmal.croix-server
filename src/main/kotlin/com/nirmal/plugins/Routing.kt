package com.nirmal.plugins

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.repository.user.UserRepository
import com.nirmal.routes.createUserRoute
import com.nirmal.routes.followUser
import com.nirmal.routes.loginUser
import com.nirmal.routes.unfollowUser
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()


    routing {
        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        followUser(followRepository)
        unfollowUser(followRepository)


    }
}
