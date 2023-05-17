package it.polito.wa2.g27.server.security

import org.springframework.http.HttpStatusCode
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
//@RequestMapping("/api")
class AuthController(private val authService: AuthService) {
    @PostMapping("/public/auth/login")
    fun login(@RequestBody authDTO: AuthDTO, br: BindingResult): Any {
        //if (br.hasErrors())
          //  throw ValidationException(br.fieldErrors, "Validation Errors")

        return authService.login(authDTO)
    }

    @DeleteMapping("/authenticated/auth/logout")
    fun logout(@RequestHeader("Authorization") token: String): HttpStatusCode {
        return authService.logout(token)
    }

    @GetMapping("/test/client/")
    fun testClient(principal: Principal){
        println("CLIENT: ${principal.name}")
    }

    @GetMapping("/test/expert/")
    fun testExpert(principal: Principal){
        println("EXPERT: ${principal.name}")
    }

    @GetMapping("/test/manager/")
    fun testManager(principal: Principal){
        println("MANAGER: ${principal.name}")
    }


}