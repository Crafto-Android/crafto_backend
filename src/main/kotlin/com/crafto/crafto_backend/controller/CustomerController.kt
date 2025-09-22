package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.service.CustomerService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customer")
class CustomerController(private val service: CustomerService) {
}
