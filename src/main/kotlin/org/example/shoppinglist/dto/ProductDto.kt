package org.example.shoppinglist.dto

import org.example.shoppinglist.model.entities.ShoppingListItem
import java.io.Serializable
import java.util.*

/**
 * DTO for {@link org.example.shoppinglist.model.entities.Product}
 */
data class ProductDto(
    val id: UUID? = null,
    val name: String? = null,
    val category: String? = null,
    val shoppingListItems: MutableSet<ShoppingListItem> = mutableSetOf()
) : Serializable