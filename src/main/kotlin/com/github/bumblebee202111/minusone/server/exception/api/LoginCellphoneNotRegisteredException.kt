package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiErrorMessages

class LoginCellphoneNotRegisteredException: ApiException(
    ApiCodes.CODE_LOGIN_CELLPHONE_NOT_REGISTERED_ERROR,
    ApiErrorMessages.MSG_WRONG_ID_OR_PASSWORD
)