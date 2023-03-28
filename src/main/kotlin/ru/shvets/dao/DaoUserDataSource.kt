package ru.shvets.dao

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.shvets.dao.DatabaseFactory.dbQuery
import ru.shvets.model.User
import ru.shvets.model.Users

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 08:49
 */

class DaoUserDataSource : UserDataSource {
    override suspend fun allUsers(): List<User> {
        return dbQuery {
            Users
                .selectAll()
                .map(::resultRowToUser)
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return dbQuery {
            Users.select { Users.username eq username }
                .map(::resultRowToUser)
                .singleOrNull()
        }
    }

    override suspend fun insertUser(user: User): Boolean {
        return Users.insert {
            it[Users.username] = user.username
            it[Users.password] = user.password
            it[Users.salt] = user.salt
        } get Users.id > 0
    }

    private fun resultRowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            username = row[Users.username],
            password = row[Users.password],
            salt = row[Users.salt],
        )
    }
}