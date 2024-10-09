package org.example.shoppinglist.model

import org.springframework.http.HttpStatus

class ApiResponse<T>(
    val _status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val _message: String? = null,
    val _data: T? = null
) {
    var status: HttpStatus = _status
    var message: String? = _message
    var data: T? = _data
}
