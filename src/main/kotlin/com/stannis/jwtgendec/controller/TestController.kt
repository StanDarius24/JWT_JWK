package com.stannis.jwtgendec.controller

import com.stannis.jwtgendec.model.Post
import com.stannis.jwtgendec.service.DecodeJWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping


@Controller
class TestController {

    @Autowired
    private lateinit var decodeJWT: DecodeJWT

    @GetMapping
    fun main(model: Model): String {
        model.addAttribute("post", Post())
        return "index"
    }

    @PostMapping
    fun save(post: Post?, model: Model): String {
        model.addAttribute("data", post?.let { decodeJWT.decode(it) })
        return "index"
    }

}
