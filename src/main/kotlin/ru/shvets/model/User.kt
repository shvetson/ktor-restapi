package ru.shvets.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 00:41
 */

@Serializable
data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val salt: String
)

object Users : Table(name = "users") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 20)
    val password = varchar("password", 150)
    val salt = varchar("salt", 150)

    override val primaryKey = PrimaryKey(id)
}