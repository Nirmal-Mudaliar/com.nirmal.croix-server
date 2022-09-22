package com.nirmal.routes

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.nirmal.data.models.User
import com.nirmal.data.request.CreateAccountRequest
import com.nirmal.di.testModule
import com.nirmal.plugins.configureSerialization
import com.nirmal.repository.user.FakeUserRepository
import com.nirmal.response.BasicApiResponse
import com.nirmal.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


internal class CreateUserRoutesKtTest: KoinTest {

    private val gson = Gson()

    private val userRepository by inject<FakeUserRepository>()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Create user, no body attached, responds with bad request`() {
        withTestApplication(
            moduleFunction = {
                install(Routing){
                    createUserRoute(userRepository)
                }
            }
        ) {
            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            )
            assertThat(request.response.status()).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `Create user, user already exists, responds with Unsuccessful`() = runBlocking{
        val user = User(
            email = "test@gmail.com",
            username = "test",
            password = "test",
            profileImageUrl = "",
            bio = "",
            githubUrl = null,
            instagramUrl = null,
            linkedinUrl = null
        )
        userRepository.createUser(
            user = user
        )
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing){
                    createUserRoute(userRepository)
                }
            }
        ) {

            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@gmail.com",
                    username = "Sdf",
                    password = "df"
                )
                setBody(gson.toJson(request))
            }
            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.USER_ALREADY_EXISTS)
        }
    }

    @Test
    fun `Create user, email is empty, responds with Unsuccessful`() = runBlocking{

        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing){
                    createUserRoute(userRepository)
                }
            }
        ) {

            val request = handleRequest(
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "",
                    username = "",
                    password = ""
                )
                setBody(gson.toJson(request))
            }
            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )
            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.FIELDS_BLANK)
        }
    }

//    @Test
//    fun `Create user, valid data, responds with successful`() {
//        withTestApplication(
//            moduleFunction = {
//                configureSerialization()
//                install(Routing) {
//                    createUserRoute(userRepository)
//                }
//            }
//        ) {
//            val request = handleRequest(
//                method = HttpMethod.Post,
//                uri = "/api/user/create"
//            ) {
//                addHeader("Content-Type", "application/json")
//                val request = CreateAccountRequest(
//                    email = "test@test.com",
//                    username = "test",
//                    password = "test"
//                )
//                setBody(gson.toJson(request))
//            }
//
//            val response = gson.fromJson(
//                request.response.content ?: "",
//                BasicApiResponse::class.java
//            )
//            assertThat(response.successful).isTrue()
//
//            runBlocking {
//                val isUserInDb = userRepository.getUserByEmail("test@gmail.com") != null
//                assertThat(isUserInDb).isTrue()
//            }
//        }
//    }


}