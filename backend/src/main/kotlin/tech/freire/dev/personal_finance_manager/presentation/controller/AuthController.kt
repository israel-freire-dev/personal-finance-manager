package tech.freire.dev.personal_finance_manager.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.freire.dev.personal_finance_manager.application.request.LoginCommand
import tech.freire.dev.personal_finance_manager.application.request.RegisterUserCommand
import tech.freire.dev.personal_finance_manager.application.response.AuthResponse
import tech.freire.dev.personal_finance_manager.application.usecase.LoginUserInputPort
import tech.freire.dev.personal_finance_manager.application.usecase.RegisterUserInputPort

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints públicos de registro e login")
class AuthController(
    private val registerUserUseCase: RegisterUserInputPort,
    private val loginUserUseCase: LoginUserInputPort
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar novo usuário", description = "Cria um usuário na plataforma e retorna JWT.")
    fun register(@RequestBody command: RegisterUserCommand): ResponseEntity<AuthResponse> {
        val response = registerUserUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login de usuário", description = "Valida credenciais e retorna JWT.")
    fun login(@RequestBody command: LoginCommand): ResponseEntity<AuthResponse> {
        val response = loginUserUseCase.execute(command)
        return ResponseEntity.ok(response)
    }
}
