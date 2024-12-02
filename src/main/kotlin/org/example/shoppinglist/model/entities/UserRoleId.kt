package org.example.shoppinglist.model.entities

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
open class UserRoleId : Serializable {
    @NotNull
    @Column(name = "user_id", nullable = false)
    open var userId: UUID? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "roles", nullable = false)
    open var roles: String? = null
    override fun hashCode(): Int = Objects.hash(userId, roles)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as UserRoleId

        return userId == other.userId &&
                roles == other.roles
    }

    companion object {
        private const val serialVersionUID = 6842643780852527993L
    }
}