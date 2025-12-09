package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus


open class CraftoExectpion(
    val code : String,
    override val message : String,
    val httpStatus : HttpStatus
):Exception(message)