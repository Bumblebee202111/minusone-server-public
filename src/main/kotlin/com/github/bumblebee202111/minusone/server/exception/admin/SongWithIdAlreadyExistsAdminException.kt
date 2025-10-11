package com.github.bumblebee202111.minusone.server.exception.admin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class SongWithIdAlreadyExistsAdminException(
    val userId: Long
):AdminApiException("Song with ID $userId already exists")