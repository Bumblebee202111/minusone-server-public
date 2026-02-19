package com.github.bumblebee202111.minusone.server.exception.admin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class SongWithIdDoesNotExistAdminException(
    val songId: Long
):AdminApiException("Song with ID $songId does not exists")