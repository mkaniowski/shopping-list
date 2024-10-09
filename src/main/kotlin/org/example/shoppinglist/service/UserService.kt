package org.example.shoppinglist.service


import org.example.shoppinglist.model.ApiResponse
import org.example.shoppinglist.model.UserUpdateRequest
import org.example.shoppinglist.model.entities.User
import org.example.shoppinglist.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

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
            password = _user.password,
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

}