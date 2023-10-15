package com.stannis.jwtgendec.controller

import com.stannis.jwtgendec.model.Encode
import com.stannis.jwtgendec.model.Post
import com.stannis.jwtgendec.service.ActionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping


@Controller
class HomeController {

    @Autowired
    private lateinit var actionManager: ActionManager

    @GetMapping("/decode")
    fun main(model: Model): String {
        return actionManager.getDecode(model)
    }

    @PostMapping("/decode")
    fun save(post: Post?, model: Model): String {
        return actionManager.postDecode(post, model)
    }

    @PostMapping("/encode")
    fun decodePost(@ModelAttribute("encode") encode: Encode?, model: Model): String {
        return actionManager.postEncode(encode, model)
    }

    @GetMapping("/encode")
    fun decodeGet(encode: Encode?, model: Model): String {
        return actionManager.getEncode(encode, model)
    }

}
