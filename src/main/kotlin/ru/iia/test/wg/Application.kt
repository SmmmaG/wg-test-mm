package ru.iia.test.wg

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import ru.iia.test.wg.routing.registerPlayerRouting
import ru.iia.test.wg.scheduler.WaitTimeChecker
import ru.iia.test.wg.service.PlayerStorage


fun main(args: Array<String>): Unit {
    if (args.isNotEmpty()) {
        try {
            PlayerStorage.storage.setGroupSize(args.get(0)?.let { it.toInt() })
            WaitTimeChecker().schedule()
            EngineMain.main(args)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    registerPlayerRouting()
}