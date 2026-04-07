package tech.freire.dev.personal_finance_manager.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.freire.dev.personal_finance_manager.application.request.CreateRecurringTemplateCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateRecurringTemplateCommand
import tech.freire.dev.personal_finance_manager.application.response.RecurringTemplateResponse
import tech.freire.dev.personal_finance_manager.application.usecase.CreateRecurringTemplateInputPort
import tech.freire.dev.personal_finance_manager.application.usecase.RecurringTemplateCrudInputPort
import tech.freire.dev.personal_finance_manager.application.usecase.DeleteRecurringTemplateUseCase
import java.util.UUID

@RestController
@RequestMapping("/api/v1/recurring-templates")
@Tag(name = "Templates Recorrentes", description = "Gerenciamento de despesas e receitas fixas")
class RecurringTemplateController(
    private val createRecurringTemplateUseCase: CreateRecurringTemplateInputPort,
    private val recurringTemplateCrudUseCase: RecurringTemplateCrudInputPort,
    private val deleteRecurringTemplateUseCase: DeleteRecurringTemplateUseCase // we can keep it injected directly or via port
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar template recorrente")
    fun create(@RequestBody command: CreateRecurringTemplateCommand): RecurringTemplateResponse {
        return createRecurringTemplateUseCase.execute(command)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar template recorrente por ID")
    fun findById(@PathVariable id: UUID): ResponseEntity<RecurringTemplateResponse> {
        val template = recurringTemplateCrudUseCase.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(template)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar templates recorrentes por usuário")
    fun findAllByUserId(@RequestParam userId: UUID): List<RecurringTemplateResponse> {
        return recurringTemplateCrudUseCase.findAllByUserId(userId)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar template recorrente")
    fun update(@PathVariable id: UUID, @RequestBody command: UpdateRecurringTemplateCommand): RecurringTemplateResponse {
        return recurringTemplateCrudUseCase.update(id, command)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar template recorrente e excluir transações futuras pendentes associadas")
    fun delete(@PathVariable id: UUID) {
        deleteRecurringTemplateUseCase.execute(id)
    }
}
