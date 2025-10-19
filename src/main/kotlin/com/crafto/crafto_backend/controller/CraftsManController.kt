package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.request.CraftsmanOfferRequest
import com.crafto.crafto_backend.response.CraftsmanOfferResponse
import com.crafto.crafto_backend.service.CraftsmanService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// POST  http://localhost:8085/crafts_man/offer

@RestController
@RequestMapping("/crafts_man")
class CraftsManController(
    private val service: CraftsmanService,
    ) {
    @PostMapping("/offer")
    fun saveCraftsmanOffer(@RequestBody body: CraftsmanOfferRequest): CraftsmanOfferResponse =
        service.saveCraftsmanOffer(body)


    @GetMapping("/offer/{customerId}")
    fun getCustomerIssuesById(@PathVariable customerId: String) =
        service.getOffersByCustomerId(customerId)
}