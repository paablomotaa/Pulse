package com.pmgdev.pulse.utils

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {

    const val AES: String = "AES"
    const val RSA: String = "RSA"


    /**
     *
     * genRSA
     *
     * Generar clave RSA
     *
     *
     */

    fun genRSA(): KeyPair {
        var keyPair = KeyPairGenerator.getInstance(RSA)
        keyPair.initialize(2048)
        return keyPair.genKeyPair()
    }

    /**
     *
     * genAES
     *
     * Generar clave AES
     *
     */
    fun genAES(): SecretKey {
        val keyAES = KeyGenerator.getInstance(AES)
        keyAES.init(256)
        return keyAES.generateKey()

    }


    /**
     *
     * desencriptarAES
     *
     * Desencripta la clave AES.
     *
     */
    fun decryptAES(encryptedKey: String, privateKey: PrivateKey): SecretKey {
        val c = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        c.init(Cipher.DECRYPT_MODE, privateKey)
        val decodedKey = c.doFinal(Base64.decode(encryptedKey, Base64.NO_WRAP))
        return SecretKeySpec(decodedKey, AES)
    }

    /**
     *
     * encriptarAES
     *
     * Encripta la clave AES.
     *
     */
    fun encryptAES(keyAes: SecretKey, publicKey: PublicKey): String {
        val c = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        c.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedKey = c.doFinal(keyAes.encoded)
        return Base64.encodeToString(encryptedKey, Base64.NO_WRAP)
    }

    /**
     *
     * decryptMessage
     *
     * Desencriptar el mensaje con la clave AES
     *
     */
    fun decryptMessage(encryptedMsg: String, claveAES: SecretKey): String {
        val ivEncrypted = Base64.decode(encryptedMsg, Base64.NO_WRAP)
        val iv = ivEncrypted.copyOfRange(0, 16)
        val encrypt = ivEncrypted.copyOfRange(16, ivEncrypted.size)
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.DECRYPT_MODE, claveAES, IvParameterSpec(iv))
        val decrypt = c.doFinal(encrypt)
        return String(decrypt, Charsets.UTF_8)
    }

    /**
     *
     * encryptMessage
     *
     * Encriptar el mensaje con la clave AES
     *
     *
     */
    fun encryptMessage(mensaje: String, claveAES: SecretKey): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, claveAES)
        val encrypted = c.doFinal(mensaje.toByteArray(Charsets.UTF_8))
        val ivEncrypted = c.iv + encrypted
        return Base64.encodeToString(ivEncrypted, Base64.NO_WRAP)

    }

    /**
     *
     * getKEYRSA
     *
     * Recoger clave RSA de EncryptedSharedPreferences
     *
     */
    fun getKEYRSA(context: Context, uid: String): PrivateKey {
        val mKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build();

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            mKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        val PrivateKey64 = sharedPreferences.getString("key_private_rsa_$uid", null)

        val privateKey = Base64.decode(PrivateKey64, Base64.NO_WRAP)

        val spec = PKCS8EncodedKeySpec(privateKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(spec)
    }

    /**
     *
     * saveKeyRSA
     *
     * Guardar clave RSA en EncryptedSharedPreferences
     *
     */
    fun saveKeyRSA(context: Context, privateKey: PrivateKey, uid: String) {
        val mKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build();

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            mKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        val privateKey64 = Base64.encodeToString(privateKey.encoded, Base64.NO_WRAP)
        sharedPreferences.edit().putString("key_private_rsa_$uid", privateKey64).apply()
    }
}
