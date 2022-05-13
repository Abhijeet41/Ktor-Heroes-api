package com.example

import com.example.plugins.*
import io.ktor.application.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureKoin()  //this should be called first in our module
    configureMonitoring()
    configureRouting()
    configureSerialization()
    configureDefaulterHeader()
    configureStatusPage()
}
