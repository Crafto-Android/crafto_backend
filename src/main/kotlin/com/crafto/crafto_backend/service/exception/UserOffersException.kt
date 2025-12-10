package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus

class UserOffersNotFound:CraftoExectpion(
    code =ErrorCode.OFFER_NOT_FOUND,
    httpStatus = HttpStatus.NOT_FOUND,
    message = "User offer not found"
)