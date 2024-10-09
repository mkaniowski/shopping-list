package org.example.shoppinglist.model

data class UserUpdateRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val isDisabled: Boolean? = null,
)
