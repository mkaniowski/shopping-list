package org.example.shoppinglist.repository

import org.example.shoppinglist.model.UserDB
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
internal interface UsersRepository : CrudRepository<UserDB, String>