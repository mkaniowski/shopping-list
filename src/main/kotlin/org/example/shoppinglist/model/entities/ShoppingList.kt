package org.example.shoppinglist.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.example.shoppinglist.model.ShoppingListProduct
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "shopping_lists", schema = "public", indexes = [
        Index(name = "idx_shopping_lists_user_id", columnList = "user_id")
    ]
)
data class ShoppingList (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @NotNull
    @Column(name = "user_id", nullable = false)
    val userId: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnore
    val user: User? = null,

    @Size(max = 128)
    @NotNull
    @Column(name = "name", nullable = false, length = 128)
    val name: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "products")
    val products: List<ShoppingListProduct>? = null,

    @NotNull
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant? = null,

    @NotNull
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null,

    @OneToMany(mappedBy = "shoppingList")
    @JsonIgnore
    val shoppingListItems: MutableSet<ShoppingListItem> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as ShoppingList
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }

    override fun toString(): String {
        return "ShoppingList(id=$id, userId=$userId, name=$name, products=$products, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}