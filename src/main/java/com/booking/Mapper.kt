package com.booking

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.restassured.internal.mapping.Jackson2Mapper
import java.lang.reflect.Type
import java.text.SimpleDateFormat


class Mapper {
    companion object {
        @JvmStatic
        var jsonMapper: io.restassured.mapper.ObjectMapper = Jackson2Mapper { _: Type?, _: String? ->
            ObjectMapper().apply {
                dateFormat = SimpleDateFormat("yyyy-MM-dd")
                registerModule(JavaTimeModule())
                registerKotlinModule()
            }
        }
    }
}