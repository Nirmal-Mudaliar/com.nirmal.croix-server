package com.nirmal

import com.nirmal.di.mainModule
import io.ktor.server.application.*
import com.nirmal.plugins.*
import org.koin.ktor.plugin.Koin
import java.nio.file.Paths


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)



@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
