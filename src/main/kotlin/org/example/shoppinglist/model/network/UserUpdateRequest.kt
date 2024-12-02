package org.example.shoppinglist.model.network

import java.time.Instant

data class UserUpdateRequest(
    val email: String? = null,
    val username: String? = null,
    val isDisabled: Instant? = null,
    val isVerified: Instant? = null
)
