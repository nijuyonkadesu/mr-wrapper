package one.karaage

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import one.karaage.data.models.MongoUserDataSource
import one.karaage.plugins.configureMonitoring
import one.karaage.plugins.configureRouting
import one.karaage.plugins.configureSecurity
import one.karaage.plugins.configureSerialization
import one.karaage.security.hashing.SHA256HashingService
import one.karaage.security.token.JwtTokenService
import one.karaage.security.token.TokenConfig
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "drive"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://guts:${mongoPw}@mrwrapper.zk09mas.mongodb.net/?retryWrites=true&w=majority"
    ).coroutine.getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property(("jwt.audience")).getString(),
        expiresIn = 365L * 24L * 60L * 60L * 1000L, // 365 days 24 hours 60 minutes 60 x 1 seconds
        secret = System.getenv("JWT_SECRET")
    )

    val hashingService = SHA256HashingService()

    configureRouting(hashingService, userDataSource, tokenService, tokenConfig)
    configureSecurity(tokenConfig)
    configureSerialization()
    configureMonitoring()
}
