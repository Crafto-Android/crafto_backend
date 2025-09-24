package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.dto.AuthorizationRequest
import com.crafto.crafto_backend.dto.AuthorizationResponse
import com.crafto.crafto_backend.service.AuthenticationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authorization")
class AuthorizationController(private val authenticationService: AuthenticationService) {

    @PostMapping
    fun authorize(@RequestBody authorizationRequest: AuthorizationRequest): AuthorizationResponse {
        return authenticationService.save(authorizationRequest)
    }

    @GetMapping
    fun findByUserId(@RequestParam userId: String): AuthorizationResponse? {
      return  authenticationService.findByUserId(userId)
    }

}