package one.karaage.data.requests

import kotlinx.serialization.Serializable

/**
 * the data provided by the user for authentication
 */
@Serializable
data class AuthRequest(
    val username: String,
    val password: String,
)
