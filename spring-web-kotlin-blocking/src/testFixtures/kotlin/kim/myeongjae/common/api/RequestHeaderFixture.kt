package kim.myeongjae.common.api

import kim.myeongjae.common.Constants
import org.springframework.http.HttpHeaders

class RequestHeaderFixture {
    companion object {
        fun create(): HttpHeaders {
            val header = HttpHeaders()
            header[Constants.HEADER_INTERNAL] = ""
            return header
        }
    }
}
