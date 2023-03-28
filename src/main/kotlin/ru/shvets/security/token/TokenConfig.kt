package ru.shvets.security.token

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 09:41
 */
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val exiresIn: Long,
    val secret:String,
)
