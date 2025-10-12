package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.request.CustomerRequest
import com.crafto.crafto_backend.service.CustomerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// POST  http://localhost:8085/customer

@RestController
@RequestMapping("/customer")
class CustomerController(private val customerService: CustomerService) {

    @PostMapping
    fun getCustomerById(@RequestBody body: CustomerRequest) = customerService.saveCustomer(body = body)
}