package org.example.shoppinglist.controller

import org.example.shoppinglist.model.RegisterUserRequest
import org.example.shoppinglist.model.entities.User
import org.example.shoppinglist.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {
    @PostMapping("/register")
    fun registerUser(@RequestBody userRegisterRequest: RegisterUserRequest): ResponseEntity<User?> {
        val res = userService.createUser(userRegisterRequest)

        return ResponseEntity(res.data, res.status)
    }
}