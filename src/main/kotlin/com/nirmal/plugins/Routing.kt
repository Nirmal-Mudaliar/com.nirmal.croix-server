package com.nirmal.plugins

import com.nirmal.repository.user.UserRepository
import com.nirmal.routes.createUserRoute
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val userRepository: UserRepository by inject()


    routing {
        createUserRoute(userRepository)
//        get("/api") {
//            call.respondText("Hello World!")
//        }
//        // Static plugin. Try to access `/static/index.html`
//        static("/static") {
//            resources("static")
//        }

    }
}
