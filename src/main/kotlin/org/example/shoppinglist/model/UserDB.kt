package org.example.shoppinglist.model

import jakarta.persistence.*
import org.example.shoppinglist.enums.UserRolesEnum
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
data class UserDB(

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "email", nullable = false, unique = true)
    val email: String = "",

    @Column(name = "username", nullable = false, unique = true)
    val username: String = "",

    @Column(name = "password", nullable = false)
    val password: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    val roles: Set<UserRolesEnum> = setOf(UserRolesEnum.ROLE_USER),

    @Column(name = "is_verified")
    val is_verified: LocalDateTime? = null,

    @Column(name = "is_disabled")
    val is_disabled: LocalDateTime? = null,

    @Column(name = "is_deleted")
    val is_deleted: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updated_at: LocalDateTime? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val created_at: LocalDateTime? = null
) {
    constructor() : this(
        UUID.randomUUID(),
        "",
        "",
        ""
    )
}
