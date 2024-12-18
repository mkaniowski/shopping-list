package org.example.shoppinglist.config

import io.github.cdimascio.dotenv.dotenv
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakAdminConfig(
    @Value("\${keycloak.realm}") private val realm: String,
    @Value("\${keycloak.resource}") private val clientId: String,
    @Value("\${keycloak.credentials.secret}") private val clientSecret: String,
) {
    @Bean
    fun keycloakAdmin(): Keycloak {
        val dotenv = dotenv()
        return KeycloakBuilder.builder()
            .serverUrl("${dotenv["KEYCLOAK_BASE_URL"]}/")
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username(dotenv["KEYCLOAK_USERNAME"])
            .password(dotenv["KEYCLOAK_PASSWORD"])
            .grantType("client_credentials")
            .build()
    }
}