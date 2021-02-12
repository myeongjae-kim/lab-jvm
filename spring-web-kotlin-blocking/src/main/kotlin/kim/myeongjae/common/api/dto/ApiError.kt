package kim.myeongjae.common.api.dto

import java.time.ZonedDateTime

data class ApiError(
    val status: Int,
    val error: String,
    val message: String,
) {
    val timestamp: ZonedDateTime = ZonedDateTime.now()
}
