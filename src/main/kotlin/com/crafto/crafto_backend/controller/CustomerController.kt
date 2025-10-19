package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.request.CustomerIssueRequest
import com.crafto.crafto_backend.request.CustomerRequest
import com.crafto.crafto_backend.response.CustomerIssueResponse
import com.crafto.crafto_backend.service.CustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

// POST  http://localhost:8085/customer
// POST  http://localhost:8085/customer/issue
// GET  http://localhost:8085/customer/issue/customer_id/{customerId}
// GET  http://localhost:8085/customer/issue/customer_issue_id/{customerIssueId}

@RestController
@RequestMapping("/customer")
class CustomerController(private val customerService: CustomerService) {

    @PostMapping
    fun saveCustomer(@RequestBody body: CustomerRequest) = customerService.saveCustomer(body = body)

    @PostMapping("/issue", consumes = ["multipart/form-data"])
    fun saveCustomerIssue(
        @RequestParam customerId: String,
        @RequestParam issueTitle: String,
        @RequestParam issueContent: String,
        @RequestParam categoryId: String,
        @RequestParam governmentId: String,
        @RequestParam districtId: String,
        @RequestParam locationDetails: String,
        @RequestParam photos: List<MultipartFile>
    ): CustomerIssueResponse {
        val body = CustomerIssueRequest(
            customerId = customerId,
            issueTitle = issueTitle,
            issueContent = issueContent,
            categoryId = categoryId,
            governmentId = governmentId,
            districtId = districtId,
            locationDetails = locationDetails
        )
        val response = customerService.saveCustomerIssue(body = body, photos)
        return response
    }

    @GetMapping("/issue/customer_id/{customerId}")
    fun getCustomerIssuesByCustomerId(@PathVariable customerId: String) =
        customerService.getCustomerIssues(customerId)

    @GetMapping("/issue/customer_issue_id/{customerIssueId}")
    fun getCustomerIssueDetailsById(@PathVariable customerIssueId: String) =
        customerService.getCustomerIssueDetails(customerIssueId)
}