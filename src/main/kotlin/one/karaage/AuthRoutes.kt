package one.karaage

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import one.karaage.data.models.User
import one.karaage.data.models.UserDataSource
import one.karaage.data.requests.AuthRequest
import one.karaage.data.responses.AuthResponse
import one.karaage.security.hashing.HashingService
import one.karaage.security.hashing.SaltedHash
import one.karaage.security.token.TokenClaim
import one.karaage.security.token.TokenConfig
import one.karaage.security.token.TokenService

/**
 * User is added to database (with salt ofc)
 */
fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource,
) {
    post("signup"){
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        // TODO: Check for blank values, small password lengths, other fancy stuffs
        // TODO: Throw appropriate error message for each fail case
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val wasAcknowledged = userDataSource.insertNewUser(user)
        if(!wasAcknowledged){
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

/**
 * Session token is provided after verification
 */
fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    post("signin"){
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if(user == null){
            call.respond(HttpStatusCode.Conflict, "incorrect username / password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            request.password,
            SaltedHash(
                user.password,
                user.salt
            )
        )
        if(!isValidPassword){
            call.respond(HttpStatusCode.Conflict, "wrong!")
            return@post
        }

        val token = tokenService.generate(
            tokenConfig,
            TokenClaim(
                "userId",
                user.id.toString()
            )
        )

        call.respond(
            HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate(){
    authenticate {
        get("authenticate"){
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo(){
    authenticate {
        get("secret"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your user id is $userId")
        }
    }
}