package com.pmgdev.pulse.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {
    private const val secretKey = "1234567890123456" // 16 caracteres (NO USAR en producción tal cual)
    private const val initVector = "abcdefghijklmnop" // 16 caracteres

    private val iv = IvParameterSpec(initVector.toByteArray(Charsets.UTF_8))
    private val keySpec: SecretKey = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
        val encrypted = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
        val original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT))
        return String(original)
    }
}