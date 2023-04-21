package one.karaage.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import one.karaage.security.token.TokenConfig

/**
 * One-Time Security configuration for our server. Sets validator and additional validator for JWT token
 */
fun Application.configureSecurity(config: TokenConfig){
    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential->
                if(credential.payload.audience.contains(config.audience)){
                    JWTPrincipal(credential.payload)
                    // JWTPrinciple is a wrapper around authenticated user
                } else null
            }
        }
    }
}