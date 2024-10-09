package org.example.shoppinglist.dto

import org.example.shoppinglist.model.User
import org.example.shoppinglist.model.UserDB

fun UserDB.toUser(): User {
    return User(
        id = this.id,
        email = this.email,
        username = this.username,
        roles = this.roles,
        isVerified = this.is_verified,
        isDisabled = this.is_disabled,
        isDeleted = this.is_deleted,
        createdAt = this.created_at,
        updatedAt = this.updated_at
    )
}

fun User.toUserDB(): UserDB {
    return UserDB(
        id = this.id,
        email = this.email,
        username = this.username,
        roles = this.roles,
        is_verified = this.isVerified,
        is_disabled = this.isDisabled,
        is_deleted = this.isDeleted,
        created_at = this.createdAt,
        updated_at = this.updatedAt
    )
}