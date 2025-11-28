package com.crafto.crafto_backend.database.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "craftsman")
data class Craftsman(
    @Id
    val id: ObjectId? = null,
    val userId: String,
    val personalInfo: PersonalInfo,
    val profilePictureUrl: String? = null,
    val verification: Verification,
    val categories: List<String>,
    val status: CraftsmanStatus = CraftsmanStatus.PENDING_VERIFICATION,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class PersonalInfo(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val address: String?
)

data class Verification(
    val idCardFront: String?,
    val idCardBack: String?,
    val workVerificationImages: List<String> = emptyList(),
    val verificationStatus: VerificationStatus = VerificationStatus.NOT_SUBMITTED,
    val verifiedAt: Instant? = null,
    val rejectionReason: String? = null
)

enum class CraftsmanStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    SUSPENDED,
    REJECTED
}

enum class VerificationStatus {
    NOT_SUBMITTED,
    PENDING,
    VERIFIED,
    REJECTED
}
