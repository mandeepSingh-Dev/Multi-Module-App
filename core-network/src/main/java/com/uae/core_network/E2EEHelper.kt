/*
package com.uae.core_network

import android.util.Base64
import android.util.Log
import jakarta.inject.Inject
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.security.PrivateKey
import java.security.PublicKey

object E2EEHelper {

    init {
        // Ensure BouncyCastle provider is present
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    */
/**
     * Generates a client ECDH key pair using prime256v1 (secp256r1) for Encryption.
     *//*

    fun generateClientKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC")
        keyPairGenerator.initialize(ECGenParameterSpec("prime256v1"))
        return keyPairGenerator.generateKeyPair()
    }

    */
/**
     * Generates an ECDSA key pair using prime256v1 (secp256r1) for Signing.
     *//*

    fun generateSigningKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC")
        keyPairGenerator.initialize(ECGenParameterSpec("prime256v1"))
        return keyPairGenerator.generateKeyPair()
    }

    */
/**
     * Converts any EC Public Key to the 65-byte uncompressed X.963 Hex format (starts with "04").
     *//*

    fun getUncompressedHexPublicKey(publicKey: PublicKey): String {
        val ecPublicKey = publicKey as ECPublicKey
        val encodedBytes = ecPublicKey.q.getEncoded(false) // false = uncompressed
        return encodedBytes.joinToString("") { "%02x".format(it) }
    }

    */
/**
     * Signs the encrypted payload string matching Node.js IEEE P-1363 format (`dsaEncoding: "ieee-p1363"`).
     *//*

    fun signPayload(encryptedPayloadBase64: String, signingPrivateKey: PrivateKey): String {
        val signer = Signature.getInstance("SHA256withECDSA", "BC")
        signer.initSign(signingPrivateKey)
        signer.update(Base64.decode(encryptedPayloadBase64, Base64.NO_WRAP))
        val rawSignature = signer.sign()

        val p1363Signature = derToP1363(rawSignature)
        return Base64.encodeToString(p1363Signature, Base64.NO_WRAP)
    }

    */
/**
     * Helper to convert standard Java ASN.1 DER signature to IEEE P-1363 (R || S).
     *//*

    private fun derToP1363(der: ByteArray): ByteArray {
        val asn1Seq = ASN1Sequence.getInstance(der)
        val r = ASN1Integer.getInstance(asn1Seq.getObjectAt(0)).positiveValue.toByteArray()
        val s = ASN1Integer.getInstance(asn1Seq.getObjectAt(1)).positiveValue.toByteArray()

        fun stripLeadingZero(bytes: ByteArray): ByteArray {
            return if (bytes.size > 32 && bytes[0] == 0.toByte()) bytes.copyOfRange(1, bytes.size) else bytes
        }

        val rClean = stripLeadingZero(r)
        val sClean = stripLeadingZero(s)

        val result = ByteArray(64)
        System.arraycopy(rClean, 0, result, 32 - rClean.size, rClean.size)
        System.arraycopy(sClean, 0, result, 64 - sClean.size, sClean.size)
        return result
    }

    */
/**
     * Replicates Node.js hkdfIOS function:
     * Salt = 32 zero bytes
     * PRK = HMAC-SHA256(Salt, SharedSecret)
     * OKM = HMAC-SHA256(PRK, 0x01) -> truncated to length (default 32)
     *//*

    private fun hkdfIOS(sharedSecret: ByteArray, length: Int = 32): ByteArray {
        val salt = ByteArray(32) { 0.toByte() }

        // PRK = HMAC-SHA256(salt, sharedSecret)
        val macPrk = Mac.getInstance("HmacSHA256")
        macPrk.init(SecretKeySpec(salt, "HmacSHA256"))
        val prk = macPrk.doFinal(sharedSecret)

        // OKM = HMAC-SHA256(prk, [0x01])
        val macOkm = Mac.getInstance("HmacSHA256")
        macOkm.init(SecretKeySpec(prk, "HmacSHA256"))
        val info = byteArrayOf(0x01)
        val okm = macOkm.doFinal(info)

        return if (okm.size >= length) okm.copyOfRange(0, length) else okm
    }

    */
/**
     * Computes the shared secret using local private key and server's uncompressed hex public key (65 bytes starting with 0x04),
     * then derives the AES key using hkdfIOS.
     *//*

    private fun getDerivedAesKey(localPrivateKey: PrivateKey, serverPublicKeyHex: String): SecretKey {
        val serverPubKeyBytes = hexStringToByteArray(serverPublicKeyHex)

        // Reconstruct X9.63 uncompressed public key to Java PublicKey object
        // DER prefix for prime256v1 uncompressed public keys
        val derPrefix = hexStringToByteArray("3059301306072A8648CE3D020106082A8648CE3D030107034200")
        val derKey = derPrefix + serverPubKeyBytes

        val keySpec = X509EncodedKeySpec(derKey)
        val keyFactory = KeyFactory.getInstance("ECDH", "BC")
        val serverPublicKey = keyFactory.generatePublic(keySpec)

        // Compute Shared Secret via KeyAgreement
        val keyAgreement = KeyAgreement.getInstance("ECDH", "BC")
        keyAgreement.init(localPrivateKey)
        keyAgreement.doPhase(serverPublicKey, true)
        val rawSharedSecret = keyAgreement.generateSecret()

        // Derive 32-byte AES key via HKDF matching Node.js
        val derivedKeyBytes = hkdfIOS(rawSharedSecret, 32)
        return SecretKeySpec(derivedKeyBytes, "AES")
    }

    */
/**
     * Encrypts payload for the server matching Node.js encryptForClient / client-side equivalent.
     * Output format: Base64( Nonce (12 bytes) + Ciphertext + AuthTag (16 bytes) )
     *//*

    fun encryptForServer(
        plaintextPayload: String,
        localPrivateKey: PrivateKey,
        serverPublicKeyHex: String
    ): String {
        val aesKey = getDerivedAesKey(localPrivateKey, serverPublicKeyHex)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
        val nonce = ByteArray(12).apply { SecureRandom().nextBytes(this) }

        val parameterSpec = GCMParameterSpec(128, nonce) // 128 bit authentication tag
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, parameterSpec)

        val plaintextBytes = plaintextPayload.toByteArray(Charsets.UTF_8)
        val ciphertextWithTag = cipher.doFinal(plaintextBytes) // Android includes auth tag at the end automatically

        // Combine Nonce + Ciphertext (which already contains AuthTag at the tail)
        val combined = nonce + ciphertextWithTag
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    */
/**
     * Decrypts payload coming from the server.
     * Expects Base64 input containing: Nonce (12 bytes) + Ciphertext + AuthTag (16 bytes)
     *//*

    fun decryptFromServer(
        encryptedBase64Payload: String,
        localPrivateKey: PrivateKey,
        serverPublicKeyHex: String
    ): String {
        val aesKey = getDerivedAesKey(localPrivateKey, serverPublicKeyHex)
        val encryptedData = Base64.decode(encryptedBase64Payload, Base64.NO_WRAP)

        // Extract Nonce (first 12 bytes) and Ciphertext+Tag (remaining bytes)
        val nonce = encryptedData.copyOfRange(0, 12)
        val ciphertextWithTag = encryptedData.copyOfRange(12, encryptedData.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC")
        val parameterSpec = GCMParameterSpec(128, nonce)
        cipher.init(Cipher.DECRYPT_MODE, aesKey, parameterSpec)

        val decryptedBytes = cipher.doFinal(ciphertextWithTag)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}




class E2EEInterceptor @Inject constructor()  : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Generate or retrieve your keys once (e.g., stored securely via EncryptedSharedPreferences)
        val encryptionKeyPair = E2EEHelper.generateClientKeyPair()
        val signingKeyPair = E2EEHelper.generateSigningKeyPair()

        val encryptionPrivateKey: PrivateKey = encryptionKeyPair.private
        val signingPrivateKey: PrivateKey = signingKeyPair.private

        val clientEncryptionPubKeyHex = E2EEHelper.getUncompressedHexPublicKey(encryptionKeyPair.public)
        val clientSigningPubKeyHex = E2EEHelper.getUncompressedHexPublicKey(signingKeyPair.public)
        val serverPublicKeyHex = "0440b086090dd8a20f02df5d8109ff8597ccf3ddf9289d53a1ee7877486a97600ca797298fcfbb0f9b5186fae50c2fcec62f5770920edba503bd24d7c668d865df"



        val originalRequest = chain.request()
        val originalBody = originalRequest.body

        Log.d("fkbnkfbnf", "Aes INtercptr")
        // If there is no body to send, proceed normally
        if (originalBody == null) {
            Log.d("fkbnkfbnf", "No Proceed")
            return chain.proceed(originalRequest)
        }
        Log.d("fkbnkfbnf", "Yes Process")

        // 1. Read the original request body content (e.g., standard JSON payload)
        val buffer = okio.Buffer()
        originalBody.writeTo(buffer)
        val originalBodyString = buffer.readUtf8()

        // 2. Encrypt the original payload for the server
        val encryptedPayload = E2EEHelper.encryptForServer(
            plaintextPayload = originalBodyString,
            localPrivateKey = encryptionPrivateKey,
            serverPublicKeyHex = serverPublicKeyHex
        )

        // 3. Sign the encrypted payload
        val signature = E2EEHelper.signPayload(
            encryptedPayloadBase64 = encryptedPayload,
            signingPrivateKey = signingPrivateKey
        )

        // 4. Construct the wrapper JSON body matching your exact backend schema
        val wrappedJson = JSONObject().apply {
            put("encryptedPayload", encryptedPayload)
            put("signature", signature)
            put("clientSigningPublicKey", clientSigningPubKeyHex)
            put("clientEncryptionPublicKey", clientEncryptionPubKeyHex)
        }


        Log.d("fkbnkfbnf", wrappedJson.toString())


        val newBodyString = wrappedJson.toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val newRequestBody = newBodyString.toRequestBody(mediaType)

        // 5. Build the modified request keeping headers and target URL intact
        val newRequest = originalRequest.newBuilder()
            .url(originalRequest.url)
            .post(newRequestBody)
            .build()

        return chain.proceed(newRequest)
    }
}*/
