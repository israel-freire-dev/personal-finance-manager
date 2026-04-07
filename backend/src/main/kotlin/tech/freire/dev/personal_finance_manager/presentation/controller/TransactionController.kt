package tech.freire.dev.personal_finance_manager.presentation.controller

import tech.freire.dev.personal_finance_manager.application.request.CreateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.response.TransactionResponse
import tech.freire.dev.personal_finance_manager.application.usecase.CreateTransactionInputPort
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.infrastructure.configuration.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * Controller REST para operações de transação.
 * "Burro" — apenas converte request para command e delega ao use case.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transações", description = "Operações de gerenciamento de transações financeiras")
class TransactionController(
    private val createTransactionUseCase: CreateTransactionInputPort,
    private val deleteRecurringTemplateUseCase: tech.freire.dev.personal_finance_manager.application.usecase.DeleteRecurringTemplateUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar transação",
        description = "Cria uma nova transação financeira vinculada a uma categoria. Suporta definir status (PAID, PENDING, etc.) e tipo (INCOME, EXPENSE)."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Transação criada com sucesso",
                content = [Content(schema = Schema(implementation = TransactionResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Erro de validação (ex: amount <= 0, categoria inexistente, categoria de outro usuário)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "422",
                description = "Estado inválido (ex: usuário não encontrado)",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun create(@RequestBody request: CreateTransactionRequest): TransactionResponse {
        val command = CreateTransactionCommand(
            userId = request.userId,
            categoryId = request.categoryId,
            description = request.description,
            amount = request.amount,
            date = request.date,
            status = request.status,
            type = request.type,
            recurringTemplateId = request.recurringTemplateId
        )
        return createTransactionUseCase.execute(command)
    }

    @DeleteMapping("/recurring/{templateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Deletar Template Recorrente",
        description = "Deleta a assinatura/recorrência e remove todas as parcelas futuras que ainda estão PENDING."
    )
    fun deleteRecurringTemplate(@PathVariable templateId: UUID) {
        deleteRecurringTemplateUseCase.execute(templateId)
    }
}

/**
 * Request body para criação de transação.
 */
data class CreateTransactionRequest(
    @field:Schema(description = "ID do usuário dono da transação", example = "550e8400-e29b-41d4-a716-446655440000")
    val userId: UUID,

    @field:Schema(description = "ID da categoria a ser vinculada", example = "660e8400-e29b-41d4-a716-446655440000")
    val categoryId: UUID,

    @field:Schema(description = "Descrição da transação", example = "Compra supermercado")
    val description: String,

    @field:Schema(description = "Valor da transação (deve ser > 0)", example = "150.50")
    val amount: BigDecimal,

    @field:Schema(description = "Data da transação", example = "2026-04-06")
    val date: LocalDate,

    @field:Schema(description = "Status da transação", example = "PAID")
    val status: TransactionStatus,

    @field:Schema(description = "Tipo da transação", example = "EXPENSE")
    val type: TransactionType,

    @field:Schema(description = "ID do template recorrente (opcional)", nullable = true)
    val recurringTemplateId: UUID? = null
)

