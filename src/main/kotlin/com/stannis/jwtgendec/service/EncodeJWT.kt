package com.stannis.jwtgendec.service

import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.stannis.jwtgendec.model.CertJsonBean
import com.stannis.jwtgendec.model.Encode
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI

@Suppress("UNCHECKED_CAST")
@Service
class EncodeJWT {

    private fun generateHeader(header: Map<String, String>): JWSHeader {
        return JWSHeader.Builder(JWSAlgorithm.RS256)
            .jwkURL(header["jku"]?.let { URI(it) })
            .keyID(header["kid"])
            .type(JOSEObjectType("JWT"))
            .build()
    }

    private fun generateRSAKeyPair(header: Map<String, String>): String {

        val rsaKey = RSAKeyGenerator(2048)
            .keyUse(KeyUse.SIGNATURE)
            .keyID(header["kid"])
            .algorithm(JWSAlgorithm.RS256)
            .generate()

        val privateJWK = RSAKey.Builder(rsaKey)
            .privateKey(rsaKey.toRSAPrivateKey())
            .keyUse(KeyUse.SIGNATURE)
            .keyID(header["kid"])
            .algorithm(JWSAlgorithm.RS256)
            .build()
            .toPrivateKey()

        val publicJWK = rsaKey.toPublicJWK()

        return "{\n" +
                "  \"keys\": [" + RSAKey.Builder(publicJWK)
            .privateKey(privateJWK)
            .build()
            .toJSONString() + "  ]\n" + "}"
    }

    private fun generateToken(certJsonBean: CertJsonBean): String? {
        return try {
            val jwkSet: JWKSet = JWKSet.load(certJsonBean.privatekey!!.byteInputStream())
            val jwk = jwkSet.keys[0]

            val header = generateHeader(certJsonBean.header as Map<String, String>)
            val claimsSet = JWTClaimsSet.parse(certJsonBean.payload)

            val signedJWT = SignedJWT(header, claimsSet)

            val signer = RSASSASigner(jwk as RSAKey)
            signedJWT.sign(signer)

            signedJWT.serialize()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun encode(encode: Encode): Map<String, String> {
        val certJsonBean = CertJsonBean()
        certJsonBean.privatekey = encode.jwk
        certJsonBean.header = ObjectMapper().readValue(encode.headerJson, Map::class.java) as Map<String, Any>?
        certJsonBean.payload = ObjectMapper().readValue(encode.bodyJson, Map::class.java) as Map<String, Any>?
        if ("" == encode.jwk) {
            certJsonBean.privatekey = generateRSAKeyPair(header = certJsonBean.header as Map<String, String>)
        }
        return mapOf( "privKey" to certJsonBean.privatekey!!,
                      "jwt" to generateToken(certJsonBean)!!)

    }

}
