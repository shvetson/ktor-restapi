package ru.shvets.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.shvets.dao.UserDataSource
import ru.shvets.model.User
import ru.shvets.request.AuthRequest
import ru.shvets.response.AuthResponse
import ru.shvets.security.hashing.HashingService
import ru.shvets.security.hashing.SaltedHash
import ru.shvets.security.token.JwtTokenService
import ru.shvets.security.token.TokenClaim
import ru.shvets.security.token.TokenConfig
import ru.shvets.security.token.TokenService

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 10:14
 */

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource,
) {
    post("signup") {
        val request = call.receive<AuthRequest>()

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPasswordShort = request.password.length < 8

        if (userDataSource.getUserByUsername(request.username) != null) {
            call.respondText("Username is already exists", status = HttpStatusCode.Conflict)
            return@post
        }

        if (areFieldsBlank || isPasswordShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
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

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: JwtTokenService,
    tokenConfig: TokenConfig,
) {
    post("/login") {
        val request = call.receive<AuthRequest>()
        val user = userDataSource.getUserByUsername(request.username)
        if (user == null) {
            call.respondText("Incorrect username or password", status = HttpStatusCode.Conflict)
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            call.respondText("Incorrect username or password", status = HttpStatusCode.Conflict)
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respondText(AuthResponse(token = token).toString(), status = HttpStatusCode.OK)

    }
}

fun Route.getInfo() {
    authenticate {
        get("user") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respondText("User id: $userId", status = HttpStatusCode.OK)
        }
    }
}