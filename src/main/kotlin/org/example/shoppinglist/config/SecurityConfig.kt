package org.example.shoppinglist.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy


@Configuration
@EnableWebSecurity
class SecurityConfig {

    private val publicEndpoints = arrayOf(
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/error",
        "/login",
        "/api/v1/auth/register"
    )

    private val userEndpoints = arrayOf(
        "/api/v1/user",
        "/api/v1/user/**"
    )

    private val adminEndpoints = arrayOf(
        "/api/v1/admin/**",
    )

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it
                    .requestMatchers(*publicEndpoints)
                    .permitAll()
                    .requestMatchers(*adminEndpoints)
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers(*publicEndpoints)
            }
            .oauth2Login { }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
            .logout {
                it.logoutSuccessHandler(keycloakLogoutHandler())
                    .permitAll()
            }
            .build()
    }


    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter().apply {
            setAuthoritiesClaimName("resource_access.shopping-list.roles")
            setAuthorityPrefix("ROLE_")
        }

        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
        }
    }

    @Bean
    fun keycloakLogoutHandler(): LogoutSuccessHandler {
        return LogoutSuccessHandler { request: HttpServletRequest,
                                      response: HttpServletResponse,
                                      authentication: Authentication? ->

            SecurityContextLogoutHandler().logout(request, response, authentication)

            val cookies = request.cookies
            if (cookies != null) {
                for (cookie in cookies) {
                    cookie.maxAge = 0
                    cookie.path = "/"
                    response.addCookie(cookie)
                }
            }

            val keycloakLogoutUrl =
                "http://localhost:8090/realms/shopping-realm/protocol/openid-connect/logout"
            val redirectUri = "http://localhost:8080/login"
            val logoutUrl = "$keycloakLogoutUrl?redirect_uri=$redirectUri"

            response.sendRedirect(logoutUrl)
        }
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        // Use the issuer URL of your Keycloak realm
        val issuerUri = "http://localhost:8090/realms/shopping-realm"
        return JwtDecoders.fromIssuerLocation(issuerUri)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    fun keycloakAuthenticationProvider(): KeycloakAuthenticationProvider {
        val keycloakAuthenticationProvider = KeycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
        return keycloakAuthenticationProvider
    }

    @Bean
    fun KeycloakConfigResolver(): KeycloakConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(keycloakAuthenticationProvider())
    }
}