package com.stannis.jwtgendec.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.stannis.jwtgendec.model.CertJsonBean
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException

@Service
class JSONReader {

    fun readJsonFile(): CertJsonBean? {
        val objectMapper: ObjectMapper
        try {
            val mapper = ObjectMapper()
            objectMapper = ObjectMapper()
            return objectMapper.readValue(File("src/main/java/data/hsm_cert.json"), CertJsonBean::class.java)
        } catch (ie: IOException) {
            ie.printStackTrace()
        }
        return null
    }

}
