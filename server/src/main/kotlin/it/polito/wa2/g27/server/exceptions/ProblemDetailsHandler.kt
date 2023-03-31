package it.polito.wa2.g27.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ProblemDetailsHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFound(e: ProfileNotFoundException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
        d.title= "Profile Not Found"
        return d
    }

    @ExceptionHandler(ProfileAlreadyExistsException::class)
    fun handleProfileNotFound(e: ProfileAlreadyExistsException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.CONFLICT, e.message!! )
        d.title= "Profile Already exists"
        return d
    }

    @ExceptionHandler(ProfileEmailModificationException::class)
    fun handleProfileNotFound(e: ProfileEmailModificationException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.CONFLICT, e.message!! )
        d.title= "Profile Email Update not possible"
        return d
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProfileNotFound(e: ProductNotFoundException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
        d.title= "Product Not Found"
        return d
    }
}

