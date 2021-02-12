package kim.myeongjae.common.api

import kim.myeongjae.common.api.dto.ApiError
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

@ControllerAdvice
class ControllerExceptionAdvice {
    @ExceptionHandler(value = [EmptyResultDataAccessException::class])
    fun handleNotFound(e: Exception): ResponseEntity<ApiError> = ResponseEntity(
        ApiError(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = e.localizedMessage
        ),
        HttpStatus.NOT_FOUND
    )
}
