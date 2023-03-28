package ru.shvets.security.token

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 09:39
 */

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim,
    ): String
}