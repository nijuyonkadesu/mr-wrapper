package one.karaage.security.token

/**
* [issuer] - the server.
* [audience] - can be a normal user, admin, any other.
* [expiresIn] - extra layer of security.
* [secret] - that client should never know.
*/
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String,
)
