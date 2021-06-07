package ru.iia.test.wg.routing

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.iia.test.wg.dto.Player
import ru.iia.test.wg.service.PlayerStorage

fun Route.playerRouting() {
    val logger: Logger = LoggerFactory.getLogger(Application::class.java)
    route("/user") {
        post {
            try {
                val latency = call.request.queryParameters["latency"]?.let { it.toDouble() } ?: Double.NaN
                val skill = call.request.queryParameters["skill"]?.let { it.toDouble() } ?: Double.NaN
                if (latency < 0 && skill < 0) {
                    call.respondText(text = "Wrong latency: $latency < 0 and skill: $skill < 0", status = HttpStatusCode.BadRequest)
                } else if (latency < 0) {
                    call.respondText(text = "Wrong latency: $latency < 0", status = HttpStatusCode.BadRequest)
                } else if (skill < 0) {
                    call.respondText(text = "Wrong latency: $skill < 0", status = HttpStatusCode.BadRequest)
                } else {
                    val name = call.request.queryParameters["name"]?.let { it } ?: ""
                    val player = Player(latency, skill, name)
                    PlayerStorage.storage.addPlayer(player)
                    call.respondText(text = "Please wait", status = HttpStatusCode.Created)
                }
            } catch (exception: Exception) {
                logger.error("error", exception)
                call.respondText(text = "Error in your request", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}

fun Application.registerPlayerRouting() {
    routing {
        playerRouting()
    }
}