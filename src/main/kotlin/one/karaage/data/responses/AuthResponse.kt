package one.karaage.data.responses

import kotlinx.serialization.Serializable
/**
* A token for client side to save for future access
*/
@Serializable
data class AuthResponse(
    val token: String
)
