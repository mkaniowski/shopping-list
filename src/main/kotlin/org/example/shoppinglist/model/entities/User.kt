package org.example.shoppinglist.model.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.example.shoppinglist.enums.UserRolesEnum
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "users",
    schema = "public",
    indexes = [Index(name = "idx_users_email", columnList = "email")],
    uniqueConstraints = [UniqueConstraint(name = "users_email_key", columnNames = ["email"])]
)
data class User(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @Size(max = 256)
    @NotNull
    @Column(name = "email", nullable = false, length = 256)
    val email: String? = null,

    @Size(max = 64)
    @NotNull
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

    @NotNull @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null,

    @NotNull @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,

    @OneToMany(mappedBy = "user")
    val shoppingLists: MutableSet<ShoppingList> = mutableSetOf(),

    @OneToMany(mappedBy = "user")
    val userRoles: MutableSet<UserRole> = mutableSetOf(),

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