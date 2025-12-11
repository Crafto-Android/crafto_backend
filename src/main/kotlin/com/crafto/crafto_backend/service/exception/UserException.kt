package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus


class UserNotFoundException:CraftoExectpion(
    code =ErrorCode.USER_NOT_FOUND,
    httpStatus = HttpStatus.NOT_FOUND,
    message = "User not found"
)

class UserAlreadyExistException:CraftoExectpion(
    code =ErrorCode.USER_ALREADY_EXIST,
    httpStatus = HttpStatus.CONFLICT,
    message = "User already exist"
)

class UserPhoneNumberAlreadyExist:CraftoExectpion(
    code = ErrorCode.USER_PHONE_NUMBER_ALREADY_EXIST,
    httpStatus = HttpStatus.CONFLICT,
    message = "User phone number already exist"
)