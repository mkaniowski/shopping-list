package org.example.shoppinglist.model

import org.example.shoppinglist.enums.UserRolesEnum
import java.time.LocalDateTime
import java.util.*

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val roles: Set<UserRolesEnum>,
    val isVerified: LocalDateTime? = null,
    val isDisabled: LocalDateTime? = null,
    val isDeleted: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
