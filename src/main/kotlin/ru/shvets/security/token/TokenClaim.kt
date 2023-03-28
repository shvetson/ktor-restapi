package ru.shvets.security.token

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 09:43
 */
data class TokenClaim(
    val name: String,
    val value: String,
)
