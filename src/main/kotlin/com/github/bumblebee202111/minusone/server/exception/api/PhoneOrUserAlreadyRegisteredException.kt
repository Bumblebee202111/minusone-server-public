package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiErrorMessages

class PhoneOrUserAlreadyRegisteredException(
    message: String = ApiErrorMessages.PHONE_ALREADY_REGISTERED,
    val errorCode: Int = ApiCodes.PHONE_ALREADY_REGISTERED
) : ApiException(errorCode, message)