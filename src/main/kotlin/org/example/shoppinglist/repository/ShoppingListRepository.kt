package org.example.shoppinglist.repository;

import org.example.shoppinglist.model.entities.ShoppingList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShoppingListRepository : JpaRepository<ShoppingList, UUID> {
}