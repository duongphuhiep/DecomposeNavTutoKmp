package com.starfruit.navtuto

import com.starfruit.util.suspendLazy
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.random.CryptographyRandom
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CryptoTest {
    class TokenCrypto {
        private val provider = CryptographyProvider.Default
        private val aes = provider.get(AES.CBC)

        // Generate or load key (store securely in production)
        private val keyData = CryptographyRandom.nextBytes(32)
        private val aesCbcKey = suspendLazy {
            aes.keyDecoder().decodeFromByteArray(AES.Key.Format.RAW, keyData)
        }
        suspend fun encryptToken(token: String): ByteArray =
            aesCbcKey().cipher().encrypt(token.encodeToByteArray())
        suspend fun decryptToken(encryptedToken: ByteArray): String =
            aesCbcKey().cipher().decrypt(encryptedToken).decodeToString()
    }

    @Test
    fun `AES CBC symmetric encryption`() = runTest {
        val crypto = TokenCrypto()
        assertEquals("Hello", crypto.decryptToken(crypto.encryptToken("Hello")))
    }
}