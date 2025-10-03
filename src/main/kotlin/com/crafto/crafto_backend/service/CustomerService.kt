package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.mapper.toEntity
import com.crafto.crafto_backend.mapper.toResponse
import com.crafto.crafto_backend.database.repository.CustomerRepository
import com.crafto.crafto_backend.request.CustomerRequest
import com.crafto.crafto_backend.dto.CustomerResponse
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository) {
    fun saveCustomer(body: CustomerRequest): CustomerResponse {
        val customer = customerRepository.save(body.toEntity())
        return customer.toResponse()
    }
}