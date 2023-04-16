package one.karaage.security.token

/**
* [name] is the name of the field, and [value] is the value contained by the field.
*/
data class TokenClaim(
    val name: String,
    val value: String,
)
