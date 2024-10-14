package org.example.shoppinglist.controller

import org.example.shoppinglist.model.entities.User
import org.example.shoppinglist.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @GetMapping("/")
    fun getUserById():
            ResponseEntity<User?> {
        val res = userService.getCurrentUser()

        return ResponseEntity(res.data, res.status)
    }

}