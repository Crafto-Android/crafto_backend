package com.crafto.crafto_backend.constant

object ApiEndpoints {

    // Base paths
    const val API_V1 = "/api/v1"
    const val API = "/api"

    // Craftsman endpoints
    object Craftsman {
        const val BASE = "/craftsman"
        const val BY_ID = "/{craftsmanId}"
        const val SETUP = "/setup"
        const val PROFILE = "/profile"
        const val STATUS = "/{craftsmanId}/status"
        const val DELETE_ACCOUNT = "/{craftsmanId}/delete"

        // Verification endpoints
        object Verify {
            const val ID_CARDS = "/{craftsmanId}/verify/id-cards"
            const val WORK_PORTFOLIO = "/{craftsmanId}/verify/work-portfolio"
            const val ADD_WORK_PORTFOLIO = "/{craftsmanId}/verify/work-portfolio/add"
        }
    }

    // TODO:Customer endpoints

}