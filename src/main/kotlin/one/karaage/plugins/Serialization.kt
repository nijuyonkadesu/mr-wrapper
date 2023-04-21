package one.karaage.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.configureSerialization() {
    // Serializers that can handle generate xml, html, json, blah blah based on the client's request
    install(ContentNegotiation){
        json()
    }
}