package com.github.bumblebee202111.minusone.server.exception.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiErrorMessages

@Suppress("SpellCheckingInspection") 
class WrrongParamException(
    message: String = ApiErrorMessages.MSG_WRRONG_PARAM
) : ApiException(ApiCodes.CODE_WRRONG_PARAM, message)