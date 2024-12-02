package org.example.shoppinglist.controller

import org.example.shoppinglist.dto.ShoppingListDto
import org.example.shoppinglist.model.entities.ShoppingList
import org.example.shoppinglist.model.network.ShoppingListItemRequest
import org.example.shoppinglist.service.ShoppingListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/shopping-list")
class ShoppingListController(private val shoppingListService: ShoppingListService) {
    @GetMapping("/{id}")
    fun getShoppingListById(@PathVariable id: String): ResponseEntity<ShoppingList?> {
        val res = shoppingListService.getShoppingListById(id)

        return ResponseEntity(res.data, res.status)
    }

    @GetMapping("")
    fun getShoppingLists(@RequestParam userId: String? = null): ResponseEntity<List<ShoppingList>> {
        val res = shoppingListService.getShoppingLists(userId)

        return ResponseEntity(res.data, res.status, )
    }

    @PostMapping("")
    fun createShoppingList(@RequestParam name: String): ResponseEntity<ShoppingList?> {
        val res = shoppingListService.createShoppingList(name)

        return ResponseEntity(res.data, res.status)
    }

    @PostMapping("/{id}")
    fun addItemToShoppingList(@PathVariable id: String, @RequestBody item: ShoppingListItemRequest): ResponseEntity<ShoppingList?> {
        val res = shoppingListService.addProductToShoppingList(id, item)

        return ResponseEntity(res.data, res.status)
    }

    @PatchMapping("/{id}")
    fun updateShoppingList(@PathVariable id: String, @RequestBody list: ShoppingListDto): ResponseEntity<ShoppingList?> {
        val res = shoppingListService.updateShoppingList(id, list)

        return ResponseEntity(res.data, res.status)
    }

    @DeleteMapping("/{id}")
    fun deleteShoppingList(@PathVariable id: String): ResponseEntity<ShoppingList?> {
        val res = shoppingListService.deleteShoppingList(id)

        return ResponseEntity(res.data, res.status)
    }

    @DeleteMapping("/{id}/product/{productId}")
    fun deleteProductFromShoppingList(@PathVariable id: String, @PathVariable productId: String): ResponseEntity<ShoppingList?> {
        val res = shoppingListService.removeProductFromShoppingList(id, productId)

        return ResponseEntity(res.data, res.status)
    }

}