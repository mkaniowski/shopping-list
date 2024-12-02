package org.example.shoppinglist.dto

import org.example.shoppinglist.model.ShoppingListProduct
import java.io.Serializable

/**
 * DTO for {@link org.example.shoppinglist.model.entities.ShoppingList}
 */
data class ShoppingListDto(val products: List<ShoppingListProduct>? = null) :
    Serializable