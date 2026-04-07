package tech.freire.dev.personal_finance_manager.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.freire.dev.personal_finance_manager.application.request.CreateUserCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateUserCommand
import tech.freire.dev.personal_finance_manager.application.response.UserResponse
import tech.freire.dev.personal_finance_manager.application.usecase.UserCrudInputPort
import tech.freire.dev.personal_finance_manager.infrastructure.configuration.ErrorResponse
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
class UserController(
    private val userCrudUseCase: UserCrudInputPort
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar usuário por ID")
    fun findById(@PathVariable id: UUID): ResponseEntity<UserResponse> {
        val user = userCrudUseCase.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar usuário")
    fun update(@PathVariable id: UUID, @RequestBody command: UpdateUserCommand): UserResponse {
        return userCrudUseCase.update(id, command)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar usuário")
    fun delete(@PathVariable id: UUID) {
        userCrudUseCase.delete(id)
    }
}
