package org.example.shoppinglist.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@GetMapping("/api/v1/user")
class UserController(private val userService: UserService){
    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: String) {
        return userService.getUserById(userId)
    }
}