package org.example.shoppinglist.dto

import org.example.shoppinglist.enums.UserRolesEnum
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * DTO for {@link org.example.shoppinglist.model.entities.User}
 */
data class UserDto(
    val id: UUID? = null,
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val roles: Set<UserRolesEnum> = setOf(UserRolesEnum.ROLE_USER),
    val isVerified: Instant? = null,
    val isDisabled: Instant? = null,
    val isDeleted: Instant? = null,
    val updatedAt: Instant? = null,
    val createdAt: Instant? = null,
    val shoppingLists: MutableSet<ShoppingListDto> = mutableSetOf()
) : Serializable