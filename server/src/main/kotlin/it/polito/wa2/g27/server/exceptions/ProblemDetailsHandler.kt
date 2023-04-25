package it.polito.wa2.g27.server.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ProblemDetailsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException
    ): ProblemDetail? {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage
        }
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, ex.message!! )
        d.title= errors.values.first()
        d.detail= errors.keys.first()
        return d
    }

    @ExceptionHandler(ProfileNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleProfileNotFound(e: ProfileNotFoundException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
        d.title= "Profile Not Found"
        return d
    }

    @ExceptionHandler(ProfileAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleProfileAlreadyExists(e: ProfileAlreadyExistsException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.CONFLICT, e.message!! )
        d.title= "Profile Already exists"
        return d
    }

    @ExceptionHandler(ProfileEmailModificationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleProfileEmailModification(e: ProfileEmailModificationException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.CONFLICT, e.message!! )
        d.title= "Profile Email Update not possible"
        return d
    }

    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleProductNotFound(e: ProductNotFoundException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
        d.title= "Product Not Found"
        return d
    }

    @ExceptionHandler(TicketNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleTicketNotFound(e: TicketNotFoundException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.NOT_FOUND, e.message!! )
        d.title= "Ticket Not Found"
        return d
    }

    @ExceptionHandler(TicketStatusNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTicketStatusNotValid(e: TicketStatusNotValidException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, e.message!! )
        d.title= "Wrong Ticket Status"
        return d
    }

    @ExceptionHandler(TicketStatusException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTicketStatus(e: TicketStatusException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, e.message!! )
        d.title= "Wrong Ticket Status"
        return d
    }

    @ExceptionHandler(TicketPriorityNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTicketPriorityNotValid(e: TicketPriorityNotValidException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, e.message!! )
        d.title= "Wrong Ticket Priority"
        return d
    }

    @ExceptionHandler(TicketBodyException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTicketBody(e: TicketBodyException): ProblemDetail {
        val d = ProblemDetail.forStatusAndDetail( HttpStatus.BAD_REQUEST, e.message!! )
        d.title= "Wrong Ticket Priority"
        return d
    }
}

