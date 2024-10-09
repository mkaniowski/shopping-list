package org.example.shoppinglist.dto

import org.example.shoppinglist.model.entities.ShoppingListItem
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * DTO for {@link org.example.shoppinglist.model.entities.ShoppingList}
 */
data class ShoppingListDto(
    val id: UUID? = null,
    val user: UserDto? = null,
    val name: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val shoppingListItems: MutableSet<ShoppingListItem> = mutableSetOf()
) : Serializable