package com.usafder.urlshortenerservice.exception

import com.usafder.urlshortenerservice.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ServiceExceptionHandler {

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(e.statusCode, e.message.toString()),
            HttpStatus.valueOf(e.statusCode)
        )
    }

}
