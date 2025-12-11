package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus

class PhoneNumberInvalidException:CraftoExectpion(
    code = ErrorCode.INVALID_PHONE_NUMBER,
    httpStatus = HttpStatus.BAD_REQUEST,
    message = "Invalid phone number"
)

class PhoneNumberRegionInvalidException:CraftoExectpion(
    code = ErrorCode.INVALID_PHONE_NUMBER_REGION,
    httpStatus = HttpStatus.BAD_REQUEST,
    message = "Invalid phone number region"
)