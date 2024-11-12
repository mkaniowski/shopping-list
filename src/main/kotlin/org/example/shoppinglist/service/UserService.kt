package org.example.shoppinglist.service

import io.github.cdimascio.dotenv.dotenv
import org.example.shoppinglist.config.SecurityConfig
import org.example.shoppinglist.enums.UserRolesEnum
import org.example.shoppinglist.model.ApiResponse
import org.example.shoppinglist.model.RegisterUserRequest
import org.example.shoppinglist.model.UserUpdateRequest
import org.example.shoppinglist.model.entities.User
import org.example.shoppinglist.repository.UserRepository
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val securityConfig: SecurityConfig,
    private val keycloakAdmin: Keycloak
) {
    private val realmResource = keycloakAdmin.realm("shopping-realm")
    private val usersResource = realmResource.users()

    fun getCurrentUser(): ApiResponse<User?> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication

        val userId = authentication.name

        System.out.println(userId)

        return getUserById(userId)
    }

    fun getUserById(userId: String): ApiResponse<User?> {
        val user = userRepository.findById(UUID.fromString(userId)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        return ApiResponse(
            _status = HttpStatus.OK,
            _data = user,
        )
    }

    fun getUsers(): ApiResponse<List<User>> {
        val users = userRepository.findAll().toList()

        return ApiResponse(_status = HttpStatus.OK, _data = users)
    }

    fun updateUser(userId: String, user: UserUpdateRequest): ApiResponse<User?> {
        val _user = userRepository.findById(UUID.fromString(userId)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        val update = User(
            id = _user.id,
            email = user.email ?: _user.email,
            username = user.username ?: _user.username,
            roles = _user.roles,
            isVerified = user.isVerified ?: _user.isVerified,
            isDisabled = user.isDisabled ?: _user.isDisabled,
            isDeleted = _user.isDeleted,
            updatedAt = LocalDateTime.now().toInstant(ZoneOffset.UTC),
            createdAt = _user.createdAt
        )

        userRepository.save(update)

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun disableUser(userId: String, isDisabled: Boolean): ApiResponse<User?> {
        val _user = userRepository.findById(UUID.fromString(userId)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        var disable: Instant? = null

        if (isDisabled) {
            disable = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        }

        val update = User(
            id = _user.id,
            email = _user.email,
            username = _user.username,
            roles = _user.roles,
            isVerified = _user.isVerified,
            isDisabled = disable,
            isDeleted = _user.isDeleted,
            updatedAt = LocalDateTime.now().toInstant(ZoneOffset.UTC),
            createdAt = _user.createdAt
        )

        userRepository.save(update)

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun deleteUser(userId: String): ApiResponse<User?> {
        val _user = userRepository.findById(UUID.fromString(userId)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        val update = User(
            id = _user.id,
            email = _user.email,
            username = _user.username,
            roles = _user.roles,
            isVerified = _user.isVerified,
            isDisabled = LocalDateTime.now().toInstant(ZoneOffset.UTC),
            isDeleted = _user.isDeleted,
            updatedAt = LocalDateTime.now().toInstant(ZoneOffset.UTC),
            createdAt = _user.createdAt
        )

        userRepository.save(update)

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun createUser(registerUserRequest: RegisterUserRequest): ApiResponse<User?> {
        val keycloakUser = UserRepresentation().apply {
            username = registerUserRequest.username
            email = registerUserRequest.email
            firstName = registerUserRequest.firstName
            lastName = registerUserRequest.lastName
            isEmailVerified = true
            isEnabled = true
        }

        val credential = CredentialRepresentation().apply {
            isTemporary = false
            type = CredentialRepresentation.PASSWORD
            value = registerUserRequest.password
        }

        keycloakUser.credentials = mutableListOf(credential)

        val response = usersResource.create(keycloakUser)

        if (response.status == 409) {
            return ApiResponse(HttpStatus.CONFLICT)
        }

        if (response.status != 201) {
            return ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        val keycloakUserId = usersResource.search(registerUserRequest.username).firstOrNull()?.id
            ?: return ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR)


        val newUser = User(
            id = UUID.fromString(keycloakUserId),
            email = registerUserRequest.email,
            username = registerUserRequest.username,
            isVerified = Instant.now(),
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        userRepository.save(newUser)

        return ApiResponse(HttpStatus.OK, "Successfully created user", newUser)

    }

}