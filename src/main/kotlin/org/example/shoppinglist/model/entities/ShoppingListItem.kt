package org.example.shoppinglist.model.entities

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Table(name = "shopping_list_items")
open class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shopping_list_id", nullable = false)
    open var shoppingList: ShoppingList? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    open var product: Product? = null

    @Column(name = "quantity_type", nullable = false, length = 32)
    open var quantityType: String? = null

    @Column(name = "quantity", nullable = false)
    open var quantity: Int? = null

    @Column(name = "notes", length = Integer.MAX_VALUE)
    open var notes: String? = null
}