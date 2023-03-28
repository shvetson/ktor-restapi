package ru.shvets.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import ru.shvets.dao.UserDataSource
import ru.shvets.routes.signIn
import ru.shvets.routes.signUp
import ru.shvets.security.hashing.HashingService
import ru.shvets.security.token.JwtTokenService
import ru.shvets.security.token.TokenConfig
import ru.shvets.security.token.TokenService

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: JwtTokenService,
    tokenConfig: TokenConfig
) {

    routing {
        signUp(
            hashingService = hashingService,
            userDataSource = userDataSource
        )

        signIn(
            userDataSource = userDataSource,
            hashingService = hashingService,
            tokenService = tokenService,
            tokenConfig = tokenConfig
        )
    }

}
