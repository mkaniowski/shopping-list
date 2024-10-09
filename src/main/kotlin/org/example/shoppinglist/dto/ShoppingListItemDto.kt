package org.example.shoppinglist.dto

import org.example.shoppinglist.dto.ProductDto
import org.example.shoppinglist.dto.ShoppingListDto
import java.io.Serializable
import java.util.*

/**
 * DTO for {@link org.example.shoppinglist.model.entities.ShoppingListItem}
 */
data class ShoppingListItemDto(
    val id: UUID? = null,
    val shoppingList: ShoppingListDto? = null,
    val product: ProductDto? = null,
    val quantityType: String? = null,
    val quantity: Int? = null,
    val notes: String? = null
) : Serializable