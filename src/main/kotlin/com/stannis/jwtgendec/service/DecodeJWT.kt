package com.stannis.jwtgendec.service

import com.stannis.jwtgendec.model.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Base64


@Service
class DecodeJWT {

    @Autowired
    private lateinit var mapGenerator: MapGenerator

    fun decodeBase64(content: String): String {
        return String(
            Base64.getDecoder()
                .decode(content)
        )
    }

    fun decodeJWT(post: Post): Array<String?> {
        val parts = post.jwt?.split(".")
        return arrayOf(
            parts?.get(0)?.let { decodeBase64(it) },
            parts?.get(1)?.let { decodeBase64(it) }
        )
    }

    fun decode(post: Post): Map<String, Map<*, *>?> {
        val result = decodeJWT(post)
        return mapOf(
            "header" to result[0]?.let { mapGenerator.convertToMap(it) },
            "body" to result[1]?.let { mapGenerator.convertToMap(it) }
        )
    }

}
