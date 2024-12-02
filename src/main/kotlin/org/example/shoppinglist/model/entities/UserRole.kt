package org.example.shoppinglist.model.entities

import jakarta.persistence.*

@Entity
@Table(name = "user_roles", schema = "public")
data class UserRole (
    @EmbeddedId
    val id: UserRoleId? = null,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as UserRole
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }

    override fun toString(): String {
        return "UserRole(id=$id, user=$user)"
    }
}