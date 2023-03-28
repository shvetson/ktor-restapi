package ru.shvets.request

import kotlinx.serialization.Serializable

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 09:36
 */

@Serializable
data class AuthRequest(
    val username: String,
    val password: String,
)