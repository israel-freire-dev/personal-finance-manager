package tech.freire.dev.personal_finance_manager.application.request

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * DTO de entrada para criação de transação.
 * Transfere dados do controller para o use case sem acoplar camadas.
 */
data class CreateTransactionCommand(
    val userId: UUID,
    val categoryId: UUID,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val status: TransactionStatus,
    val type: TransactionType,
    val recurringTemplateId: UUID? = null
)
