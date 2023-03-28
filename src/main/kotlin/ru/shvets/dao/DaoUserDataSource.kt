package ru.shvets.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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
        return dbQuery {
            Users.insert {
                it[Users.username] = user.username
                it[Users.password] = user.password
                it[Users.salt] = user.salt
            } get Users.id > 0
        }
    }

//    override suspend fun insertUser(user: User): Boolean {
//        val insertStatement = transaction{
//            Users.insert {
//                it[username] = user.username
//                it[password] = user.password
//                it[salt] = user.salt
//            }
//        }
//        return insertStatement.resultedValues != null
//    }

    private fun resultRowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            username = row[Users.username],
            password = row[Users.password],
            salt = row[Users.salt],
        )
    }
}