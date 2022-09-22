package com.nirmal.di

import com.nirmal.repository.user.FakeUserRepository
import org.koin.dsl.module

internal val testModule = module {
    single {
        FakeUserRepository()
    }
}