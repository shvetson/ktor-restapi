package ru.shvets.response

import kotlinx.serialization.Serializable

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 09:37
 */

@Serializable
data class AuthResponse(
    val token: String,
)