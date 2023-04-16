package one.karaage.security.token

interface TokenService {
    /**
     * Generates a new token
     * */
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}