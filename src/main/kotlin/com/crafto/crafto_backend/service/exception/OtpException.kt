package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus

class OtpExpiredException:CraftoExectpion(
    code = ErrorCode.OTP_EXPIRED,
    httpStatus = HttpStatus.BAD_REQUEST,
    message = "Otp expired"
)

class OtpInvalidException:CraftoExectpion(
    code = ErrorCode.OTP_INVALID,
    httpStatus = HttpStatus.BAD_REQUEST,
    message = "Otp invalid"
)