package ru.shvets.security.hashing

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 09:56
 */

interface HashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash
    fun verify(value: String, saltedHash: SaltedHash): Boolean
}