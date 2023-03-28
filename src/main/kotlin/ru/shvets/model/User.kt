package ru.shvets.model

import org.jetbrains.exposed.sql.Table

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 00:41
 */

data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val salt: String
)

object Users : Table(name = "users") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 20)
    val password = varchar("password", 20)
    val salt = varchar("salt", 30)

    override val primaryKey = PrimaryKey(id)
}