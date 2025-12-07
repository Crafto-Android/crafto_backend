package com.crafto.crafto_backend.database.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("customer_issue")
data class CustomerIssue(
    @Id
    val id: ObjectId? = null,
    val customerId: String,
    val title: String,
    val description: String,
    val status: CustomerIssueStatus= CustomerIssueStatus.SUBMITTED,
    val categoryId: ObjectId,
    val location: IssueLocation,
    val photos: List<String>,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class IssueLocation(
    val governorateId: String,
    val governorateName: String,
    val districtId: String,
    val districtName: String,
    val detailedLocation: String
)

enum class CustomerIssueStatus {
    SUBMITTED,
    RECEIVING_OFFERS,
    CRAFTSMAN_SELECTED,
    IN_PROGRESS,
    DONE,
    CANCELED
}