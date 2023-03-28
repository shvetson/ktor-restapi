package ru.shvets

import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import ru.shvets.dao.DaoUserDataSource
import ru.shvets.dao.DatabaseFactory
import ru.shvets.dao.UserDataSource
import ru.shvets.plugins.*
import ru.shvets.security.hashing.SHA256HashingService
import ru.shvets.security.token.JwtTokenService
import ru.shvets.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseFactory.init()

    val userDataSource: UserDataSource = DaoUserDataSource()
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        exiresIn = 365L * 1000 * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureSerialization()
    configureMonitoring()
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
}
