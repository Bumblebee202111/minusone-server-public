package com.github.bumblebee202111.minusone.server.exception.admin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

class ResourceNotFoundException(message: String) : AdminApiException(message)