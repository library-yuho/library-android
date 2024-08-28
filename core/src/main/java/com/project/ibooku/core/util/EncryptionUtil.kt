package com.project.ibooku.core.util

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val IV_SIZE = 16 // 16 bytes IV for AES

    // 고정된 32바이트(256비트) 키를 사용
    private val fixedKey = "your_fixed_key_32_byte_length!!!".toByteArray(Charsets.UTF_8)

    fun getSecretKey(): SecretKeySpec {
        return SecretKeySpec(fixedKey, ALGORITHM)
    }

    fun encrypt(plainText: String): String {
        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv) // 랜덤 IV 생성
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        val ivAndEncryptedText = iv + encryptedBytes // IV를 암호화된 텍스트 앞에 붙임
        return Base64.encodeToString(ivAndEncryptedText, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String): String {
        val secretKey = getSecretKey()
        val ivAndEncryptedText = Base64.decode(encryptedText, Base64.DEFAULT)
        val iv = ivAndEncryptedText.sliceArray(0 until IV_SIZE)
        val encryptedBytes = ivAndEncryptedText.sliceArray(IV_SIZE until ivAndEncryptedText.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}