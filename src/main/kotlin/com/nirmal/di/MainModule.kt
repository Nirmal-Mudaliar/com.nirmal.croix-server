package com.nirmal.di

import com.nirmal.data.repository.follow.FollowRepository
import com.nirmal.data.repository.follow.FollowRepositoryImpl
import com.nirmal.data.repository.post.PostRepository
import com.nirmal.data.repository.post.PostRepositoryImpl
import com.nirmal.data.repository.user.UserRepository
import com.nirmal.data.repository.user.UserRepositoryImpl
import com.nirmal.service.FollowService
import com.nirmal.service.PostService
import com.nirmal.service.UserService
import com.nirmal.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single {
        UserService(get())
    }

    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single {
        FollowService(get())
    }

    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single {
        PostService(get())
    }

}