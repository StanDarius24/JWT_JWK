package com.stannis.jwtgendec.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class MapGenerator {

    fun convertToMap(text: String): Map<*, *>? {
        return ObjectMapper().readValue(text, Map::class.java)
    }

}
