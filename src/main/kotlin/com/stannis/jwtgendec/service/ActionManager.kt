package com.stannis.jwtgendec.service

import com.stannis.jwtgendec.model.Encode
import com.stannis.jwtgendec.model.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.ui.Model

@Service
class ActionManager {

    @Autowired
    private lateinit var decodeJWT: DecodeJWT

    @Autowired
    private lateinit var encodeJWT: EncodeJWT

    fun getDecode(model: Model): String {
        model.addAttribute("post", Post())
        model.addAttribute("encode", Encode())
        return "index"
    }

    fun postDecode(post: Post?, model: Model): String {
        model.addAttribute("data", post?.let { decodeJWT.decode(it) })
        model.addAttribute("post", post)
        model.addAttribute("encode", Encode())
        return "index"
    }

    fun getEncode(encode: Encode?, model: Model): String {
        val result = encode?.let { encodeJWT.encode(it) }
        encode!!.encodedJWT = result!!["jwt"]
        encode.jwk = result["privKey"]
        model.addAttribute("post", Post())
        model.addAttribute("encode", encode)
        return "index"
    }

    fun postEncode(encode: Encode?, model: Model): String {
        val result = encode?.let { encodeJWT.encode(it) }
        encode!!.encodedJWT = result!!["jwt"]
        encode.jwk = result["privKey"]
        model.addAttribute("post", Post())
        model.addAttribute("encode", encode)
        return "index"
    }

}
