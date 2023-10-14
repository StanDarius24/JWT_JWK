package com.stannis.jwtgendec.service

import com.stannis.jwtgendec.model.CertJsonBean
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.StringReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

@Service
class EncodeJWT {

    private lateinit var jsonReader: JSONReader

    @Throws(Exception::class)
    private fun getPrivateKey(privateKey: String): PrivateKey {
        // Read in the key into a String
        val pkcs8Lines = StringBuilder()
        val rdr = BufferedReader(StringReader(privateKey))
        var line: String?
        while (rdr.readLine().also { line = it } != null) {
            pkcs8Lines.append(line)
        }
        // Remove the "BEGIN" and "END" lines, as well as any whitespace
        var pkcs8Pem = pkcs8Lines.toString()
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "")
        pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "")
        pkcs8Pem = pkcs8Pem.replace("\\s+".toRegex(), "")
        // Base64 decode the result
        val pkcs8EncodedBytes: ByteArray = Base64.getDecoder().decode(pkcs8Pem)
        // extract the private key
        val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedBytes)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(keySpec)
    }

    private fun generateToken(certJsonBean: CertJsonBean): String? {
        var token: String? = null
        val privateKey: String?
        try {
            privateKey = certJsonBean.privatekey
            val payload = certJsonBean.payload
            val header = certJsonBean.header
            // get the private key from encoded key.
            val pvtKey = getPrivateKey(privateKey!!)
            token = if (certJsonBean.privatekey!!.isNotEmpty()) {
                Jwts.builder().claims(payload).header().add(header).and()
                    .signWith(pvtKey, SignatureAlgorithm.RS256).compact()
            } else {
                "Something went wrong!!"
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return token
    }

    private fun verifyToken(token: String, publicKey: PublicKey) {
        var claims: Claims?
        try {
            Jwts.parser().verifyWith(publicKey)
            println()
        } catch (e: java.lang.Exception) {
            claims = null
        }
    }

    fun encode() {
        val certJsonBean: CertJsonBean? = jsonReader.readJsonFile()
        val token: String? = generateToken(certJsonBean!!)
        println(token)
    }

}
