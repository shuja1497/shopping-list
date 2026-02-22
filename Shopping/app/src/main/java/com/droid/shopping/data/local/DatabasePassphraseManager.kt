package com.droid.shopping.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import androidx.core.content.edit

/**
 * Manages the SQLCipher database passphrase.
 *
 * The passphrase is generated once on first launch and stored in
 * EncryptedSharedPreferences backed by the Android Keystore, so it
 * never appears in plaintext on disk.
 */
object DatabasePassphraseManager {

    private const val PREFS_FILE = "db_secure_prefs"
    private const val KEY_PASSPHRASE = "db_passphrase"
    private const val PASSPHRASE_LENGTH = 32

    fun getPassphrase(context: Context): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFS_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

        val existing = prefs.getString(KEY_PASSPHRASE, null)
        if (existing != null) {
            return existing.toByteArray()
        }

        val passphrase = generatePassphrase()
        prefs.edit { putString(KEY_PASSPHRASE, String(passphrase)) }
        return passphrase
    }

    private fun generatePassphrase(): ByteArray {
        val random = SecureRandom()
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return ByteArray(PASSPHRASE_LENGTH) { chars[random.nextInt(chars.length)].code.toByte() }
    }
}
