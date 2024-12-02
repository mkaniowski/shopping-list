package org.example.shoppinglist.service

import org.example.shoppinglist.dto.ShoppingListDto
import org.example.shoppinglist.model.ApiResponse
import org.example.shoppinglist.model.ShoppingListProduct
import org.example.shoppinglist.model.entities.ShoppingList
import org.example.shoppinglist.model.network.ShoppingListItemRequest
import org.example.shoppinglist.repository.ShoppingListRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import org.springframework.security.core.authority.SimpleGrantedAuthority

@ExtendWith(MockitoExtension::class)
class ShoppingListServiceTest {

    @Mock
    private lateinit var shoppingListRepository: ShoppingListRepository

    @Mock
    private lateinit var securityContext: SecurityContext

    @Mock
    private lateinit var authentication: Authentication

    @InjectMocks
    private lateinit var shoppingListService: ShoppingListService

    private fun setupMockAuthentication(userId: String, roles: List<String> = listOf()) {
        `when`(securityContext.authentication).thenReturn(authentication)
        `when`(authentication.name).thenReturn(userId)

        // Stub roles only when they are passed
        if (roles.isNotEmpty()) {
            `when`(authentication.authorities).thenReturn(roles.map { SimpleGrantedAuthority(it) })
        }

        SecurityContextHolder.setContext(securityContext)
    }

    @Test
    fun `should return shopping list for authorized user`() {
        val userId = UUID.randomUUID().toString()
        val shoppingListId = UUID.randomUUID()
        val shoppingList = ShoppingList(
            id = shoppingListId,
            userId = UUID.fromString(userId),
            name = "Groceries",
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )
        setupMockAuthentication(userId)

        `when`(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(shoppingList))

        val response = shoppingListService.getShoppingListById(shoppingListId.toString())

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(shoppingList, response.data)
        verify(shoppingListRepository, times(1)).findById(shoppingListId)
    }

    @Test
    fun `should return UNAUTHORIZED if user is not authenticated`() {
        SecurityContextHolder.clearContext()

        val response = shoppingListService.getShoppingListById(UUID.randomUUID().toString())

        assertEquals(HttpStatus.UNAUTHORIZED, response.status)
        assertNull(response.data)
    }

    @Test
    fun `should create a new shopping list`() {
        // Arrange
        val userId = UUID.randomUUID().toString()
        setupMockAuthentication(userId) // Stub only what is needed for this test

        val shoppingList = ShoppingList(
            id = UUID.randomUUID(),
            userId = UUID.fromString(userId),
            name = "Groceries",
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )

        `when`(shoppingListRepository.save(any())).thenReturn(shoppingList)

        // Act
        val response = shoppingListService.createShoppingList("Groceries")

        // Assert
        assertEquals(HttpStatus.CREATED, response.status)
        assertNotNull(response.data)
        assertEquals("Groceries", response.data?.name)
        assertEquals(UUID.fromString(userId), response.data?.userId)
    }


    @Test
    fun `should delete shopping list if user is authorized`() {
        val userId = UUID.randomUUID().toString()
        val shoppingListId = UUID.randomUUID()
        val shoppingList = ShoppingList(
            id = shoppingListId,
            userId = UUID.fromString(userId),
            name = "To Delete",
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )
        setupMockAuthentication(userId)

        `when`(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(shoppingList))
        doNothing().`when`(shoppingListRepository).delete(shoppingList)

        val response = shoppingListService.deleteShoppingList(shoppingListId.toString())

        assertEquals(HttpStatus.NO_CONTENT, response.status)
        verify(shoppingListRepository, times(1)).findById(shoppingListId)
        verify(shoppingListRepository, times(1)).delete(shoppingList)
    }

    @Test
    fun `should return FORBIDDEN if user tries to delete another user's list`() {
        val userId = UUID.randomUUID().toString()
        val anotherUserId = UUID.randomUUID().toString()
        val shoppingListId = UUID.randomUUID()
        val shoppingList = ShoppingList(
            id = shoppingListId,
            userId = UUID.fromString(anotherUserId),
            name = "Other User's List",
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )
        setupMockAuthentication(userId)

        `when`(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(shoppingList))

        val response = shoppingListService.deleteShoppingList(shoppingListId.toString())

        assertEquals(HttpStatus.FORBIDDEN, response.status)
        verify(shoppingListRepository, times(1)).findById(shoppingListId)
        verify(shoppingListRepository, never()).delete(any())
    }

    @Test
    fun `should update a shopping list`() {
        // Arrange
        val userId = UUID.randomUUID().toString()
        val shoppingListId = UUID.randomUUID()
        setupMockAuthentication(userId)

        val existingShoppingList = ShoppingList(
            id = shoppingListId,
            userId = UUID.fromString(userId),
            name = "Old List",
            products = emptyList(),
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )

        val updatedDto = ShoppingListDto(
            products = listOf(
                ShoppingListProduct(
                    id = UUID.randomUUID(),
                    name = "Milk",
                    quantity = 2,
                    quantityType = "Liters"
                )
            )
        )

        `when`(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(existingShoppingList))
        `when`(shoppingListRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        val response = shoppingListService.updateShoppingList(shoppingListId.toString(), updatedDto)

        // Assert
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.data)
        assertEquals("Old List", response.data?.name)
        assertEquals(1, response.data?.products?.size)
        assertEquals("Milk", response.data?.products?.get(0)?.name)
    }


    @Test
    fun `should add a product to a shopping list`() {
        // Arrange
        val userId = UUID.randomUUID().toString()
        val shoppingListId = UUID.randomUUID()
        setupMockAuthentication(userId)

        val existingShoppingList = ShoppingList(
            id = shoppingListId,
            userId = UUID.fromString(userId),
            name = "Groceries",
            products = mutableListOf(),
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )

        val newProduct = ShoppingListItemRequest(
            name = "Bread",
            quantity = 1,
            quantityType = "Loaf"
        )

        `when`(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(existingShoppingList))
        `when`(shoppingListRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        val response = shoppingListService.addProductToShoppingList(shoppingListId.toString(), newProduct)

        // Assert
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.data)
        assertEquals(1, response.data?.products?.size)
        assertEquals("Bread", response.data?.products?.get(0)?.name)
    }



    @Test
    fun `should remove a product from a shopping list`() {
        // Arrange
        val userId = UUID.randomUUID().toString()
        val shoppingListId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        setupMockAuthentication(userId)

        val existingProduct = ShoppingListProduct(
            id = productId,
            name = "Eggs",
            quantity = 12,
            quantityType = "Pieces"
        )

        val existingShoppingList = ShoppingList(
            id = shoppingListId,
            userId = UUID.fromString(userId),
            name = "Groceries",
            products = mutableListOf(existingProduct),
            createdAt = Date().toInstant(),
            updatedAt = Date().toInstant()
        )

        `when`(shoppingListRepository.findById(shoppingListId)).thenReturn(Optional.of(existingShoppingList))
        `when`(shoppingListRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        val response = shoppingListService.removeProductFromShoppingList(shoppingListId.toString(), productId.toString())

        // Assert
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.data)
        assertTrue(response.data?.products?.isEmpty() == true)
    }


}
