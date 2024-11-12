package org.example.shoppinglist.model.entities

import jakarta.persistence.*
import org.example.shoppinglist.enums.UserRolesEnum
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Column(name = "email", nullable = false, length = 256)
    val email: String? = null,

    @Column(name = "username", nullable = false, length = 64)
    val username: String? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "roles", nullable = false)
    var roles: MutableSet<UserRolesEnum> = mutableSetOf(UserRolesEnum.ROLE_USER),

    @Column(name = "is_verified")
    val isVerified: Instant? = null,

    @Column(name = "is_disabled")
    val isDisabled: Instant? = null,

    @Column(name = "is_deleted")
    val isDeleted: Instant? = null,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,

    @OneToMany(mappedBy = "user")
    val shoppingLists: MutableSet<ShoppingList> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as User
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , email = $email , username = $username , roles = $roles , isVerified = $isVerified , isDisabled = $isDisabled , isDeleted = $isDeleted , updatedAt = $updatedAt , createdAt = $createdAt )"
    }

    fun clearRoles() {
        roles.clear()
    }
}