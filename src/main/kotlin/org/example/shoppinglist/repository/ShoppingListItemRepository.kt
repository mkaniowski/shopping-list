package org.example.shoppinglist.repository;

import org.example.shoppinglist.model.entities.ShoppingListItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShoppingListItemRepository : JpaRepository<ShoppingListItem, UUID> {
}