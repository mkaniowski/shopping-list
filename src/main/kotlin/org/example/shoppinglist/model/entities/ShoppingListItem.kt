package org.example.shoppinglist.model.entities

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Table(
    name = "shopping_list_items", schema = "public", indexes = [
        Index(name = "idx_shopping_list_items_shopping_list_id", columnList = "shopping_list_id")
    ]
)
data class ShoppingListItem (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    val id: UUID? = null,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shopping_list_id", nullable = false)
    val shoppingList: ShoppingList? = null,

    @Size(max = 256)
    @NotNull
    @Column(name = "product_name", nullable = false, length = 256)
    val productName: String? = null,

    @Size(max = 32)
    @NotNull
    @Column(name = "quantity_type", nullable = false, length = 32)
    val quantityType: String? = null,

    @NotNull
    @Column(name = "quantity", nullable = false)
    val quantity: Int? = null,

    @Column(name = "notes", length = Integer.MAX_VALUE)
    val notes: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as ShoppingListItem
        return id != null && id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }

    override fun toString(): String {
        return "ShoppingListItem(id=$id, shoppingList=$shoppingList, productName=$productName, quantityType=$quantityType, quantity=$quantity, notes=$notes)"
    }
}