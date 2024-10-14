package org.example.shoppinglist

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class ShoppingListApplication

fun main(args: Array<String>) {
    runApplication<ShoppingListApplication>(*args)
}
