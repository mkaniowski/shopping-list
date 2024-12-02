package org.example.shoppinglist.service

import org.example.shoppinglist.dto.ShoppingListDto
import org.example.shoppinglist.model.ApiResponse
import org.example.shoppinglist.model.ShoppingListProduct
import org.example.shoppinglist.model.entities.ShoppingList
import org.example.shoppinglist.model.network.ShoppingListItemRequest
import org.example.shoppinglist.repository.ShoppingListRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShoppingListService(
    private val shoppingListRepository: ShoppingListRepository
) {


    private fun getAuthentication(): Authentication? {
        return SecurityContextHolder.getContext().authentication
    }

    private fun getCurrentUserRoles(): List<String> {
        val authentication: Authentication = getAuthentication() ?: return emptyList()
        return authentication.authorities.map { it.authority }
    }

    fun getShoppingListById(id: String): ApiResponse<ShoppingList?> {
        val userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)

        val shoppingList = shoppingListRepository.findById(UUID.fromString(id)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        if (shoppingList.userId != UUID.fromString(userId) && !getCurrentUserRoles().contains("ROLE_ADMIN")) {
            return ApiResponse(_status = HttpStatus.FORBIDDEN, _data = null)
        }

        return ApiResponse(
            _status = HttpStatus.OK,
            _data = shoppingList,
        )
    }

    fun getShoppingLists(userId: String? = null): ApiResponse<List<ShoppingList>> {
        val shoppingLists = if (getCurrentUserRoles().contains("ROLE_ADMIN") && userId != null) {
            shoppingListRepository.findByUserId(UUID.fromString(userId))
        } else if (getCurrentUserRoles().contains("ROLE_ADMIN") && userId == null) {
            shoppingListRepository.findAll().toList()
        } else {
            val _userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)
            shoppingListRepository.findByUserId(UUID.fromString(_userId))
        }

        println(shoppingLists)

        return ApiResponse(_status = HttpStatus.OK, _data = shoppingLists)
    }

    fun createShoppingList(name: String): ApiResponse<ShoppingList?> {
        val userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)

        val shoppingList = ShoppingList(
            userId = UUID.fromString(userId),
            name = name,
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )

        shoppingListRepository.save(shoppingList)

        return ApiResponse(_status = HttpStatus.CREATED, _data = shoppingList)
    }

    fun deleteShoppingList(id: String): ApiResponse<ShoppingList?> {
        val userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)

        val shoppingList = shoppingListRepository.findById(UUID.fromString(id)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        if (shoppingList.userId != UUID.fromString(userId) && !getCurrentUserRoles().contains("ROLE_ADMIN")) {
            return ApiResponse(_status = HttpStatus.FORBIDDEN, _data = null)
        }

        shoppingListRepository.delete(shoppingList)

        return ApiResponse(_status = HttpStatus.NO_CONTENT, _data = null)
    }

    fun updateShoppingList(id: String, list: ShoppingListDto): ApiResponse<ShoppingList?> {
        val userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)

        val shoppingList = shoppingListRepository.findById(UUID.fromString(id)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        if (shoppingList.userId != UUID.fromString(userId) && !getCurrentUserRoles().contains("ROLE_ADMIN")) {
            return ApiResponse(_status = HttpStatus.FORBIDDEN, _data = null)
        }

        val update = ShoppingList(
            id = shoppingList.id,
            userId = shoppingList.userId,
            name = shoppingList.name,
            products = list.products,
            createdAt = shoppingList.createdAt,
            updatedAt = Date().toInstant()
        )

        shoppingListRepository.save(update)

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun addProductToShoppingList(id: String, product: ShoppingListItemRequest): ApiResponse<ShoppingList?> {
        val userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)

        val shoppingList = shoppingListRepository.findById(UUID.fromString(id)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        if (shoppingList.userId != UUID.fromString(userId) && !getCurrentUserRoles().contains("ROLE_ADMIN")) {
            return ApiResponse(_status = HttpStatus.FORBIDDEN, _data = null)
        }

        val products = shoppingList.products?.toMutableList() ?: mutableListOf()

        val productMap = ShoppingListProduct(
            id = UUID.randomUUID(),
            name = product.name,
            quantity = product.quantity,
            quantityType = product.quantityType
        )
        products.add(productMap)


        val update = ShoppingList(
            id = shoppingList.id,
            userId = shoppingList.userId,
            name = shoppingList.name,
            products = products,
            createdAt = shoppingList.createdAt,
            updatedAt = Date().toInstant()
        )

        shoppingListRepository.save(update)

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }

    fun removeProductFromShoppingList(id: String, productId: String): ApiResponse<ShoppingList?> {
        val userId = getAuthentication()?.name ?: return ApiResponse(_status = HttpStatus.UNAUTHORIZED, _data = null)

        val shoppingList = shoppingListRepository.findById(UUID.fromString(id)).orElse(null)
            ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)

        if (shoppingList.userId != UUID.fromString(userId) && !getCurrentUserRoles().contains("ROLE_ADMIN")) {
            return ApiResponse(_status = HttpStatus.FORBIDDEN, _data = null)
        }

        val products = shoppingList.products?.toMutableList() ?: mutableListOf()

        val product = products.find { it.id.toString() == productId } ?: return ApiResponse(_status = HttpStatus.NOT_FOUND, _data = null)
        products.remove(product)

        val update = ShoppingList(
            id = shoppingList.id,
            userId = shoppingList.userId,
            name = shoppingList.name,
            products = products,
            createdAt = shoppingList.createdAt,
            updatedAt = Date().toInstant()
        )

        shoppingListRepository.save(update)

        return ApiResponse(_status = HttpStatus.OK, _data = update)
    }
}