package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.response.ApiCode

class NotFoundException: ApiException(
    code = ApiCode.CODE_404_NOT_FOUND, message = "no resource"
)