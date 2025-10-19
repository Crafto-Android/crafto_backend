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
// GET  http://localhost:8085/crafts_man/offer/customer_id/{customerId}
// GET  http://localhost:8085/crafts_man/offer/customer_issue_id/{customerIssueId}

@RestController
@RequestMapping("/crafts_man")
class CraftsManController(
    private val service: CraftsmanService,
    )
{
    @PostMapping("/offer")
    fun saveCraftsmanOffer(@RequestBody body: CraftsmanOfferRequest): CraftsmanOfferResponse =
        service.saveCraftsmanOffer(body)


    @GetMapping("/offer/customer_id/{customerId}")
    fun getCraftsmanOffersByCustomerId(@PathVariable customerId: String) =
        service.getOffersByCustomerId(customerId)

    @GetMapping("/offer/customer_issue_id/{customerIssueId}")
    fun getCraftsmanOffersByCustomerIssueId(@PathVariable customerIssueId: String) =
        service.getOffersByCustomerIssueId(customerIssueId)
}