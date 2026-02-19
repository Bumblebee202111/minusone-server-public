package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes

class NotFoundException: ApiException(
    code = ApiCodes.CODE_NOT_FOUND, message = "no resource"
)