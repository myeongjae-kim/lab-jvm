package kim.myeongjae.common.api

import kim.myeongjae.common.api.dto.ApiError
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionAdvice {
    @ExceptionHandler(value = [EmptyResultDataAccessException::class])
    fun handleNotFound(e: Exception): ResponseEntity<ApiError> = ResponseEntity(
        ApiError(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = e.localizedMessage,
        ),
        HttpStatus.NOT_FOUND,
    )

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleInvalidParam(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> = ResponseEntity(
        ApiError(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = when (val objectError = ex.bindingResult.allErrors.first()) {
                is FieldError -> "${objectError.field} ${objectError.defaultMessage}"
                else -> objectError.defaultMessage!!
            },
        ),
        HttpStatus.BAD_REQUEST,
    )
}
