package one.karaage.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import one.karaage.authenticate
import one.karaage.data.models.UserDataSource
import one.karaage.getSecretInfo
import one.karaage.security.hashing.HashingService
import one.karaage.security.token.TokenConfig
import one.karaage.security.token.TokenService
import one.karaage.signIn
import one.karaage.signUp

fun Application.configureRouting(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
