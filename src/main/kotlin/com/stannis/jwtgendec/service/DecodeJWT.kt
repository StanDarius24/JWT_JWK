package com.stannis.jwtgendec.service

import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.SignedJWT
import com.stannis.jwtgendec.model.Post
import org.springframework.stereotype.Service
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.util.*


@Service
class DecodeJWT {

    fun decodeBase64(content: String): String {
        return String(
            Base64.getDecoder()
                .decode(content)
        )
    }

    @Throws(java.lang.Exception::class)
    fun jwkSetToRSAPublicKey(jwkSet: JWKSet): PublicKey {
        val jwk = jwkSet.keys[0]

        if (jwk is RSAKey) {

            return jwk.toRSAPublicKey()
        }
        throw Error("jwk is no Rsakey")
    }

    @Throws(Exception::class)
    fun verifyJwt(jwtString: String, jwksUrl: String): Boolean {
        val jwt = SignedJWT.parse(jwtString)
        val jwkSet: JWKSet = JWKSet.load(jwksUrl.byteInputStream())
        val publicKey = jwkSetToRSAPublicKey(jwkSet)

        if (publicKey is RSAPublicKey) {
            val verifier = RSASSAVerifier(publicKey)
            return jwt.verify(verifier)
        }
        throw Error("Wrong RSAPublicKEY")
    }

    fun decodeJWT(post: Post): Array<String?> {
        val parts = post.jwt?.split(".")
        return arrayOf(
            parts?.get(0)?.let { decodeBase64(it) },
            parts?.get(1)?.let { decodeBase64(it) }
        )
    }

    fun decode(post: Post): Map<String, *> {
        val result = decodeJWT(post)
        val signature: Boolean? = try {
            post.jwt?.let { post.jwk?.let { it1 -> verifyJwt(it, it1) } }
        } catch (e: Exception) {
            false
        }
        return mapOf(
            "header" to result[0],
            "body" to result[1],
            "signature" to signature
        )
    }

}
