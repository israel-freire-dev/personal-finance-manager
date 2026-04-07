package tech.freire.dev.personal_finance_manager.adapter.inbound.web

import tech.freire.dev.personal_finance_manager.application.dto.CreateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.dto.TransactionResponse
import tech.freire.dev.personal_finance_manager.application.port.input.CreateTransactionInputPort
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
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
class TransactionController(
    private val createTransactionUseCase: CreateTransactionInputPort
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
}

/**
 * Request body para criação de transação.
 */
data class CreateTransactionRequest(
    val userId: UUID,
    val categoryId: UUID,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val status: TransactionStatus,
    val type: TransactionType,
    val recurringTemplateId: UUID? = null
)
