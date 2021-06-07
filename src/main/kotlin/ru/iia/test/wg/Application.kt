package ru.iia.test.wg

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import ru.iia.test.wg.routing.registerPlayerRouting
import ru.iia.test.wg.scheduler.WaitTimeChecker


fun main(args: Array<String>): Unit {
    WaitTimeChecker().schedule()
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    registerPlayerRouting()
}