package com.github.bumblebee202111.minusone.server.response

val LoginCellphoneNotRegisteredErrorResponse = AppErrorResponse(
    501,
    "该手机号尚未注册"
)


val LoginWrongIdOrPasswordErrorResponse = AppErrorResponse(
    502,
    "用户名或密码错误"
)