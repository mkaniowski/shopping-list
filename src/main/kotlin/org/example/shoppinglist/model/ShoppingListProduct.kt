package org.example.shoppinglist.model

import java.util.*

data class ShoppingListProduct(
    val id: UUID,
    val name: String,
    val quantity: Int,
    val quantityType: String,
)
