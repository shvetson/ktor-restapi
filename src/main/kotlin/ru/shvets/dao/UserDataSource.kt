package ru.shvets.dao

import ru.shvets.model.User

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  28.03.2023 08:47
 */

interface UserDataSource {
    suspend fun allUsers(): List<User>
    suspend fun getUserByUsername(username:String): User?
    suspend fun insertUser(user: User): Boolean
}