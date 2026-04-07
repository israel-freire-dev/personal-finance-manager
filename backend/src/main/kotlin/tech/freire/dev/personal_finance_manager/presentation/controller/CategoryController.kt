package tech.freire.dev.personal_finance_manager.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.freire.dev.personal_finance_manager.application.request.CreateCategoryCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateCategoryCommand
import tech.freire.dev.personal_finance_manager.application.response.CategoryResponse
import tech.freire.dev.personal_finance_manager.application.usecase.CategoryCrudInputPort
import java.util.UUID

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categorias", description = "Gerenciamento de categorias")
class CategoryController(
    private val categoryCrudUseCase: CategoryCrudInputPort
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar categoria")
    fun create(@RequestBody command: CreateCategoryCommand): CategoryResponse {
        return categoryCrudUseCase.create(command)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar categoria por ID")
    fun findById(@PathVariable id: UUID): ResponseEntity<CategoryResponse> {
        val category = categoryCrudUseCase.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(category)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar categorias por usuário")
    fun findAllByUserId(@RequestParam userId: UUID): List<CategoryResponse> {
        return categoryCrudUseCase.findAllByUserId(userId)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar categoria")
    fun update(@PathVariable id: UUID, @RequestBody command: UpdateCategoryCommand): CategoryResponse {
        return categoryCrudUseCase.update(id, command)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar categoria")
    fun delete(@PathVariable id: UUID) {
        categoryCrudUseCase.delete(id)
    }
}
