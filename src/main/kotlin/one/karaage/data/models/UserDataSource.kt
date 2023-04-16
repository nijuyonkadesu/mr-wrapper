package one.karaage.data.models

interface UserDataSource {
    suspend fun getUserByUsername(userName: String): User?
    suspend fun insertNewUser(user: User): Boolean

}