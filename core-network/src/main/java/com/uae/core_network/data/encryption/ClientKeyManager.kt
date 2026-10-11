import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.Mac
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64


data class EncryptionResult(
    val encryptedPayload: String,
    val signature: String
)


object CryptoManager {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    private const val SIGNING_ALIAS = "client_signing_key"
    private const val ENCRYPTION_ALIAS = "client_encryption_key"

    private const val CURVE = "secp256r1"

    private const val AES_KEY_SIZE = 32
    private const val GCM_TAG_LENGTH = 128
    private const val IV_SIZE = 12

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
    }


    // ============================================================
    // 1. Generate client signing + encryption keys
    // ============================================================

    fun generateKeysIfNeeded() {

        // Generate signing key pair only if it doesn't already exist.
        if (!keyStore.containsAlias(SIGNING_ALIAS)) {
            generateSigningKeyPair()
        }

        // Generate encryption key pair only if it doesn't already exist.
        if (!keyStore.containsAlias(ENCRYPTION_ALIAS)) {
            generateEncryptionKeyPair()
        }
    }


    // ============================================================
    // 2. Generate client signing key pair
    // ============================================================

    private fun generateSigningKeyPair() {

        val generator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            SIGNING_ALIAS,
            KeyProperties.PURPOSE_SIGN or
                    KeyProperties.PURPOSE_VERIFY
        )
            .setAlgorithmParameterSpec(
                ECGenParameterSpec(CURVE)
            )
            .setDigests(
                KeyProperties.DIGEST_SHA256
            )
            .build()

        generator.initialize(spec)
        generator.generateKeyPair()
    }


    // ============================================================
    // 3. Generate client encryption key pair
    // ============================================================

    private fun generateEncryptionKeyPair() {

        val generator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            ANDROID_KEYSTORE
        )

        val spec = KeyGenParameterSpec.Builder(
            ENCRYPTION_ALIAS,
            KeyProperties.PURPOSE_AGREE_KEY
        )
            .setAlgorithmParameterSpec(
                ECGenParameterSpec(CURVE)
            )
            .build()

        generator.initialize(spec)
        generator.generateKeyPair()
    }


    // ============================================================
    // 4. Get client signing key pair
    // ============================================================

    fun getSigningKeyPair(): KeyPair {

        val entry = keyStore.getEntry(
            SIGNING_ALIAS,
            null
        ) as KeyStore.PrivateKeyEntry

        return KeyPair(
            entry.certificate.publicKey,
            entry.privateKey
        )
    }


    // ============================================================
    // 5. Get client encryption key pair
    // ============================================================

    fun getEncryptionKeyPair(): KeyPair {

        val entry = keyStore.getEntry(
            ENCRYPTION_ALIAS,
            null
        ) as KeyStore.PrivateKeyEntry

        return KeyPair(
            entry.certificate.publicKey,
            entry.privateKey
        )
    }


    // ============================================================
    // 6. Get client encryption PUBLIC key
    //
    // This is the key you send in:
    //
    // x-client-encryption-key
    //
    // Format:
    // 04 + X + Y
    // ============================================================

    fun getClientEncryptionPublicKey(): String {

        val publicKey = getEncryptionKeyPair().public
                as ECPublicKey

        val x = publicKey.w.affineX
            .toFixed32ByteArray()

        val y = publicKey.w.affineY
            .toFixed32ByteArray()

        return "04" +
                x.toHex() +
                y.toHex()
    }


    // ============================================================
    // 7. Get client signing PUBLIC key
    //
    // Backend can use this to verify our signature.
    // ============================================================

    fun getClientSigningPublicKey(): String {

        val publicKey = getSigningKeyPair().public
                as ECPublicKey

        val x = publicKey.w.affineX
            .toFixed32ByteArray()

        val y = publicKey.w.affineY
            .toFixed32ByteArray()

        return "04" +
                x.toHex() +
                y.toHex()
    }


    // ============================================================
    // 8. Convert server public key from:
    //
    // 04 + X + Y
    //
    // to Android PublicKey
    // ============================================================

    private fun serverPublicKeyFromHex(
        publicKeyHex: String
    ): PublicKey {

        require(publicKeyHex.startsWith("04")) {
            "Server public key must be an uncompressed EC key"
        }

        val key = publicKeyHex.substring(2)

        require(key.length == 128) {
            "Invalid secp256r1 public key length"
        }

        val x = BigInteger(
            key.substring(0, 64),
            16
        )

        val y = BigInteger(
            key.substring(64, 128),
            16
        )

        val parameters = java.security.AlgorithmParameters
            .getInstance("EC")
            .apply {
                init(
                    ECGenParameterSpec(CURVE)
                )
            }
            .getParameterSpec(
                java.security.spec.ECParameterSpec::class.java
            )

        val publicKeySpec = ECPublicKeySpec(
            ECPoint(x, y),
            parameters
        )

        return KeyFactory
            .getInstance("EC")
            .generatePublic(publicKeySpec)
    }


    // ============================================================
    // 9. Create ECDH shared secret
    //
    // Client Encryption PRIVATE KEY
    //             +
    // Server PUBLIC KEY
    //             ↓
    //       Shared Secret
    // ============================================================

    private fun generateSharedSecret(
        serverPublicKeyHex: String
    ): ByteArray {

        val clientPrivateKey =
            getEncryptionKeyPair().private

        val serverPublicKey =
            serverPublicKeyFromHex(serverPublicKeyHex)

        val keyAgreement =
            KeyAgreement.getInstance("ECDH")

        keyAgreement.init(clientPrivateKey)

        keyAgreement.doPhase(
            serverPublicKey,
            true
        )

        return keyAgreement.generateSecret()
    }


    // ============================================================
    // 10. Derive AES key from ECDH shared secret
    //
    // Shared Secret
    //      ↓
    //    HKDF
    //      ↓
    //   AES-256 key
    //
    // IMPORTANT:
    // salt/info MUST match backend implementation.
    // ============================================================

    private fun deriveAesKey(
        sharedSecret: ByteArray,
        salt: ByteArray? = null,
        info: ByteArray = ByteArray(0)
    ): ByteArray {

        // HKDF-Extract
        val actualSalt =
            salt ?: ByteArray(32)

        val mac = Mac.getInstance("HmacSHA256")

        mac.init(
            SecretKeySpec(
                actualSalt,
                "HmacSHA256"
            )
        )

        val pseudoRandomKey =
            mac.doFinal(sharedSecret)


        // HKDF-Expand

        val expandMac =
            Mac.getInstance("HmacSHA256")

        expandMac.init(
            SecretKeySpec(
                pseudoRandomKey,
                "HmacSHA256"
            )
        )

        val output = ByteArray(AES_KEY_SIZE)

        var previous = ByteArray(0)
        var offset = 0
        var counter: Byte = 1

        while (offset < AES_KEY_SIZE) {

            expandMac.reset()

            expandMac.update(previous)
            expandMac.update(info)
            expandMac.update(counter)

            previous = expandMac.doFinal()

            val copyLength = minOf(
                previous.size,
                AES_KEY_SIZE - offset
            )

            System.arraycopy(
                previous,
                0,
                output,
                offset,
                copyLength
            )

            offset += copyLength
            counter++
        }

        return output
    }


    // ============================================================
    // 11. Encrypt API payload
    //
    // Plain JSON
    //      ↓
    // ECDH shared secret
    //      ↓
    // HKDF
    //      ↓
    // AES-256-GCM
    //      ↓
    // Base64
    // ============================================================

    fun encryptPayload(
        plainJson: String,
        serverPublicKeyHex: String
    ): String {

        val sharedSecret =
            generateSharedSecret(
                serverPublicKeyHex
            )

        val aesKey =
            deriveAesKey(sharedSecret)

        // Generate a random 12-byte IV.
        val iv = ByteArray(IV_SIZE)

        java.security.SecureRandom()
            .nextBytes(iv)

        val cipher =
            Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(
                aesKey,
                "AES"
            ),
            GCMParameterSpec(
                GCM_TAG_LENGTH,
                iv
            )
        )

        val cipherText =
            cipher.doFinal(
                plainJson.toByteArray(Charsets.UTF_8)
            )

        /*
         * Backend must know this format.
         *
         * Here:
         *
         * IV + CipherText + GCM Tag
         *
         * Android's GCM doFinal() returns:
         *
         * CipherText + Authentication Tag
         */

        val combined = ByteBuffer
            .allocate(iv.size + cipherText.size)
            .put(iv)
            .put(cipherText)
            .array()

        return Base64
            .getEncoder()
            .encodeToString(combined)
    }


    // ============================================================
    // 12. Sign encrypted payload
    //
    // encryptedPayload
    //        ↓
    // Client Signing PRIVATE KEY
    //        ↓
    // ECDSA SHA256
    //        ↓
    // Base64 signature
    // ============================================================

    // ============================================================
// 12. Sign encrypted payload
//
// encryptedPayload
//        ↓
// Client Signing PRIVATE KEY
//        ↓
// ECDSA SHA256
//        ↓
// DER signature
//        ↓
// Convert DER → raw R || S
//        ↓
// Base64
// ============================================================

    fun signPayload(
        encryptedPayload: String
    ): String {

        val privateKey =
            getSigningKeyPair().private

        val signature =
            Signature.getInstance("SHA256withECDSA")

        signature.initSign(privateKey)

        // ✅ Sign the DECODED bytes (IV + ciphertext + tag),
        // not the UTF-8 text of the base64 string.
        // This must match the backend:
        //   const encryptedBuffer = Buffer.from(encryptedPayload, "base64");
        //   verify.update(encryptedBuffer);
        val decodedBytes = Base64.getDecoder().decode(encryptedPayload)

        signature.update(decodedBytes)

        val derSignature = signature.sign()

        val rawSignature =
            derToRawEcdsaSignature(derSignature)

        return Base64
            .getEncoder()
            .encodeToString(rawSignature)
    }


    // ============================================================
// Convert DER ECDSA signature to raw R || S
//
// DER:
// 30 xx 02 xx [R] 02 xx [S]
//
// Raw:
// [32-byte R][32-byte S]
//
// Total = 64 bytes
// ============================================================

    private fun derToRawEcdsaSignature(
        derSignature: ByteArray
    ): ByteArray {

        require(
            derSignature.isNotEmpty() &&
                    derSignature[0] == 0x30.toByte()
        ) {
            "Invalid DER ECDSA signature"
        }

        var offset = 2

        // --------------------------------------------------------
        // Read R
        // --------------------------------------------------------

        require(
            derSignature[offset] == 0x02.toByte()
        ) {
            "Invalid R in ECDSA signature"
        }

        val rLength =
            derSignature[offset + 1].toInt()

        var r = derSignature.copyOfRange(
            offset + 2,
            offset + 2 + rLength
        )

        offset += 2 + rLength

        // --------------------------------------------------------
        // Read S
        // --------------------------------------------------------

        require(
            derSignature[offset] == 0x02.toByte()
        ) {
            "Invalid S in ECDSA signature"
        }

        val sLength =
            derSignature[offset + 1].toInt()

        var s = derSignature.copyOfRange(
            offset + 2,
            offset + 2 + sLength
        )

        // --------------------------------------------------------
        // Remove DER sign padding
        //
        // DER may add 00 at beginning when the highest bit
        // is set because INTEGER is signed.
        // --------------------------------------------------------

        if (r.size > 32 && r[0] == 0.toByte()) {
            r = r.copyOfRange(
                1,
                r.size
            )
        }

        if (s.size > 32 && s[0] == 0.toByte()) {
            s = s.copyOfRange(
                1,
                s.size
            )
        }

        require(r.size <= 32) {
            "Invalid R length: ${r.size}"
        }

        require(s.size <= 32) {
            "Invalid S length: ${s.size}"
        }

        // --------------------------------------------------------
        // Left pad R and S to exactly 32 bytes
        // --------------------------------------------------------

        val rawR = ByteArray(32)

        System.arraycopy(
            r,
            0,
            rawR,
            32 - r.size,
            r.size
        )

        val rawS = ByteArray(32)

        System.arraycopy(
            s,
            0,
            rawS,
            32 - s.size,
            s.size
        )

        // --------------------------------------------------------
        // R || S = 64 bytes
        // --------------------------------------------------------

        return rawR + rawS
    }
    // ============================================================
    // 13. Encrypt + Sign
    //
    // This is the function interceptor will call.
    // ============================================================

    fun encryptAndSign(
        plainJson: String,
        serverPublicKeyHex: String
    ): EncryptionResult {

        val encryptedPayload =
            encryptPayload(
                plainJson = plainJson,
                serverPublicKeyHex = serverPublicKeyHex
            )

        val signature =
            signPayload(
                encryptedPayload
            )

        return EncryptionResult(
            encryptedPayload = encryptedPayload,
            signature = signature
        )
    }


    // ============================================================
    // Helper: BigInteger → exactly 32 bytes
    // ============================================================

    private fun BigInteger.toFixed32ByteArray(): ByteArray {

        val bytes = toByteArray()

        return when {

            bytes.size == 32 -> {
                bytes
            }

            bytes.size == 33 &&
                    bytes[0] == 0.toByte() -> {
                bytes.copyOfRange(1, 33)
            }

            bytes.size < 32 -> {
                ByteArray(32 - bytes.size) +
                        bytes
            }

            else -> {
                error("Invalid EC coordinate size")
            }
        }
    }


    // ============================================================
    // Helper: ByteArray → Hex
    // ============================================================

    private fun ByteArray.toHex(): String {
        return joinToString("") {
            "%02x".format(it)
        }
    }



    // ============================================================
// Decrypt API response
//
// encryptedPayload
//      ↓
// Base64 decode
//      ↓
// Extract IV
//      ↓
// ECDH shared secret
//      ↓
// HKDF
//      ↓
// AES-GCM decrypt
//      ↓
// Original JSON
// ============================================================

    fun decryptPayload(
        encryptedPayload: String,
        serverPublicKeyHex: String
    ): String {

        // Get the same ECDH shared secret that was used
        // while encrypting the request.
        val sharedSecret = generateSharedSecret(
            serverPublicKeyHex = serverPublicKeyHex
        )

        // Derive the same AES key.
        val aesKey = deriveAesKey(
            sharedSecret = sharedSecret
        )

        // Decode Base64 response.
        val encryptedBytes = Base64
            .getDecoder()
            .decode(encryptedPayload)

        // We stored:
        //
        // IV + CipherText + AuthenticationTag
        //
        // IV = first 12 bytes.
        require(encryptedBytes.size > IV_SIZE) {
            "Invalid encrypted payload"
        }

        val iv = encryptedBytes.copyOfRange(
            0,
            IV_SIZE
        )

        // Remaining bytes contain:
        //
        // CipherText + GCM Authentication Tag
        val cipherText = encryptedBytes.copyOfRange(
            IV_SIZE,
            encryptedBytes.size
        )

        val cipher = Cipher.getInstance(
            "AES/GCM/NoPadding"
        )

        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(
                aesKey,
                "AES"
            ),
            GCMParameterSpec(
                GCM_TAG_LENGTH,
                iv
            )
        )

        val decryptedBytes = cipher.doFinal(
            cipherText
        )

        return String(
            decryptedBytes,
            Charsets.UTF_8
        )
    }
}