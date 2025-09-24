package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.dto.AuthenticationRequest
import com.crafto.crafto_backend.dto.AuthenticationResponse
import com.crafto.crafto_backend.service.AuthenticationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authentication")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping
    fun authenticate(@RequestBody authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        return authenticationService.save(authenticationRequest)
    }

    @GetMapping
    fun findByUserId(@RequestParam userId: String): AuthenticationResponse? {
      return  authenticationService.findByUserId(userId)
    }

}