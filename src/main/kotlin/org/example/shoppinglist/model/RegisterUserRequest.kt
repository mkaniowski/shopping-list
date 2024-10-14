package org.example.shoppinglist.model

data class RegisterUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)
