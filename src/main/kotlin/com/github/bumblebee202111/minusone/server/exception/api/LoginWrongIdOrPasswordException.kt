package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiErrorMessages

class LoginWrongIdOrPasswordException: ApiException(
    ApiCodes.CODE_LOGIN_WRONG_ID_OR_PASSWORD_ERROR,
    ApiErrorMessages.MSG_WRONG_ID_OR_PASSWORD
)