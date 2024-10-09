package org.example.shoppinglist.model.entities

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "products")
open class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @Column(name = "name", nullable = false, length = 256)
    open var name: String? = null

    @Column(name = "category", length = 128)
    open var category: String? = null

    @OneToMany(mappedBy = "product")
    open var shoppingListItems: MutableSet<ShoppingListItem> = mutableSetOf()
}