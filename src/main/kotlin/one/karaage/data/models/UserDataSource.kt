package one.karaage.data.models

interface UserDataSource {
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertNewUser(user: User): Boolean

}