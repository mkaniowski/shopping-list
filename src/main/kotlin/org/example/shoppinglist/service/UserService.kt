package org.example.shoppinglist.service


import org.example.shoppinglist.dto.toUser
import org.example.shoppinglist.dto.toUserDB
import org.example.shoppinglist.model.ApiResponse
import org.example.shoppinglist.model.User
import org.example.shoppinglist.model.UserUpdateRequest
import org.example.shoppinglist.repository.UsersRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(private val usersRepository: UsersRepository) {

    fun getUserById(userId: String): ApiResponse<User?> {
        val user = usersRepository.findById(userId).getOrNull()
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        return ApiResponse(
            _status = HttpStatus.OK,
            _data = user.toUser(),
        )
    }

    fun getUsers(): ApiResponse<List<User>> {
        val users = usersRepository.findAll().toList().map { it.toUser() }

        return ApiResponse(_status = HttpStatus.OK, _data = users)
    }

    fun updateUser(userId: String, user: UserUpdateRequest): ApiResponse<User?> {
        val _user = usersRepository.findById(userId).getOrNull()
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        val update = User(
            id = _user.id,
            email = user.email ?: _user.email,
            username = user.username ?: _user.username,
            roles = _user.roles,
            isVerified = _user.is_verified,
            isDisabled = _user.is_disabled,
            isDeleted = _user.is_deleted,
            updatedAt = LocalDateTime.now(),
            createdAt = _user.created_at
        )

        usersRepository.save(update.toUserDB())

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun disableUser(userId: String, isDisabled: Boolean): ApiResponse<User?> {
        val _user = usersRepository.findById(userId).getOrNull()
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        var disable: LocalDateTime? = null

        if (isDisabled) {
            disable = LocalDateTime.now()
        }

        val update = User(
            id = _user.id,
            email = _user.email,
            username = _user.username,
            roles = _user.roles,
            isVerified = _user.is_verified,
            isDisabled = disable,
            isDeleted = _user.is_deleted,
            updatedAt = LocalDateTime.now(),
            createdAt = _user.created_at
        )

        usersRepository.save(update.toUserDB())

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun deleteUser(userId: String): ApiResponse<User?> {
        val _user = usersRepository.findById(userId).getOrNull()
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        val update = User(
            id = _user.id,
            email = _user.email,
            username = _user.username,
            roles = _user.roles,
            isVerified = _user.is_verified,
            isDisabled = LocalDateTime.now(),
            isDeleted = _user.is_deleted,
            updatedAt = LocalDateTime.now(),
            createdAt = _user.created_at
        )

        usersRepository.save(update.toUserDB())

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

}