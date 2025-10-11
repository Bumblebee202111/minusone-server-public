package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes

class LogoutException(
    message: String = "系统错误"
) : ApiException(ApiCodes.CODE_301_LOGOUT, message)