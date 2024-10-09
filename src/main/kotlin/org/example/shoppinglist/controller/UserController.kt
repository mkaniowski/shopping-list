package org.example.shoppinglist.controller

import org.example.shoppinglist.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService){
    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: String): Unit? {
        return userService.getUserById(userId)
    }
}