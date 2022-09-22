package com.nirmal.routes


import com.nirmal.repository.user.UserRepository
import com.nirmal.data.models.User
import com.nirmal.data.request.CreateAccountRequest
import com.nirmal.response.BasicApiResponse
import com.nirmal.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.createUserRoute(userRepository: UserRepository) {

//    route("/api/posts"){
//        get {
//            call.respondText(
//                "Hellodf "
//            )
//        }
//    }

    route("/api/user/create") {
        post {
            val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (request.email.isBlank() || request.username.isBlank() || request.password.isBlank()) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.FIELDS_BLANK,
                    )
                )
                return@post
            }
            val userExists = userRepository.getUserByEmail(request.email) != null
            if (userExists) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_ALREADY_EXISTS,
                    )
                )
                return@post
            }

            userRepository.createUser(
                user = User(
                    email = request.email,
                    username = request.username,
                    password = request.password,
                    profileImageUrl = "",
                    bio = "",
                    githubUrl = null,
                    linkedinUrl = null,
                    instagramUrl = null
                )
            )
            call.respond(
                BasicApiResponse(successful = true)
            )
        }
    }
}

