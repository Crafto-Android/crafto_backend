package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus

class UserIssueNotFound:CraftoExectpion(
    code =ErrorCode.ISSUE_NOT_FOUND,
    httpStatus = HttpStatus.NOT_FOUND,
    message = "User issue not found"
)