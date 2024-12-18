package org.example.shoppinglist.repository;

import org.example.shoppinglist.model.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
}