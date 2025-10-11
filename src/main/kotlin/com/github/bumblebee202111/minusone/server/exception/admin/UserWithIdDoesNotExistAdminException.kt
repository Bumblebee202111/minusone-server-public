package com.github.bumblebee202111.minusone.server.exception.admin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserWithIdDoesNotExistAdminException(
    val userId: Long
):AdminApiException("User with ID $userId does not exists")