package one.karaage

import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import one.karaage.data.models.MongoUserDataSource
import one.karaage.data.models.User
import one.karaage.plugins.configureRouting
import one.karaage.plugins.customerRouting
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

    val mongoUserDataSource = MongoUserDataSource(db)
    GlobalScope.launch {
        val user = User(
            userName = "test",
            password = "test",
            salt = "sugar"
        )
        mongoUserDataSource.insertNewUser(user)
    }
    configureRouting()
    customerRouting()
}
