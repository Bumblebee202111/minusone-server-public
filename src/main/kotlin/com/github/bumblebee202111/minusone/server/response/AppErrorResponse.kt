package com.github.bumblebee202111.minusone.server.response

data class AppErrorResponse(
    val code: Int,
    val msg: String?
) {
    @Suppress("unused")
    
    

    val message = msg
}

