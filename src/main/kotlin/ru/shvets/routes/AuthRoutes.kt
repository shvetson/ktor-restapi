package ru.shvets.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.shvets.dao.UserDataSource
import ru.shvets.model.User
import ru.shvets.request.AuthRequest
import ru.shvets.security.hashing.HashingService

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 10:14
 */

fun Route.singUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
){
    post("signup") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPasswordShort = request.password.length < 8

        if (userDataSource.getUserByUsername(request.username) != null) {
            call.respondText ("Username is already exists", status = HttpStatusCode.Conflict)
            return@post
        }

        if (areFieldsBlank || isPasswordShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = request.password,
            salt = saltedHash.salt
        )

        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        } else {
            call.respond(HttpStatusCode.OK)
        }

    }
}