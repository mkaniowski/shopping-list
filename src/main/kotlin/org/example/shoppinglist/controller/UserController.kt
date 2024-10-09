package org.example.shoppinglist.controller

import org.example.shoppinglist.model.User
import org.example.shoppinglist.model.UserUpdateRequest
import org.example.shoppinglist.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {
    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: String):
            ResponseEntity<User?> {
        val res = userService.getUserById(userId)

        return ResponseEntity(res.data, res.status)
    }

    @GetMapping("/")
    fun getAllUsers(): ResponseEntity<List<User>> {
        val res = userService.getUsers()

        return ResponseEntity(res.data, res.status)
    }

    @PatchMapping("/users/{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody user: UserUpdateRequest): ResponseEntity<User> {
        val res = userService.updateUser(userId = userId, user = user)

        return ResponseEntity(res.data, res.status)
    }

    @PatchMapping("/users/disable/{userId}")
    fun disableUser(@PathVariable userId: String, @RequestParam isDisabled: Boolean): ResponseEntity<User> {
        val res = userService.disableUser(userId = userId, isDisabled = isDisabled)

        return ResponseEntity(res.data, res.status)
    }

    @DeleteMapping("/users/{userId}")
    fun deleteUser(@PathVariable userId: String): ResponseEntity<User> {
        val res = userService.deleteUser(userId = userId)

        return ResponseEntity(res.data, res.status)
    }
}