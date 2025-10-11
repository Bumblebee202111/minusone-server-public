package com.github.bumblebee202111.minusone.server.security

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


fun ByteArray.aesEcbPkcs5paddingEncrypt(key: ByteArray): ByteArray {
    val keySpec = SecretKeySpec(key, "AES")
    return Cipher.getInstance("AES/ECB/PKCS5PADDING").run {
        init(Cipher.ENCRYPT_MODE, keySpec)
        doFinal(this@aesEcbPkcs5paddingEncrypt)
    }
}

fun ByteArray.aesEcbPkcs5paddingDecrypt(key: ByteArray): ByteArray {
    val keySpec = SecretKeySpec(key, "AES")
    return Cipher.getInstance("AES/ECB/PKCS5PADDING").run {
        init(Cipher.DECRYPT_MODE, keySpec)
        doFinal(this@aesEcbPkcs5paddingDecrypt)
    }
}
