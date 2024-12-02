package org.example.shoppinglist.model.network

data class ShoppingListItemRequest (
    val name: String,
    val quantity: Int,
    val quantityType: String,
)