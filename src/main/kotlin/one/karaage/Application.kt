package one.karaage

import io.ktor.server.application.Application
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

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)
// TODO: Refer ktor site for SSL / https tutorial in CIO
// TODO: letsencrypt.org certificate for https authserver
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
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

    configureSecurity(tokenConfig)
    configureSerialization()
    configureMonitoring()
    configureRouting(hashingService, userDataSource, tokenService, tokenConfig)
}
// TODO: Use https://github.com/kosi-libs/Kodein KODEINDI