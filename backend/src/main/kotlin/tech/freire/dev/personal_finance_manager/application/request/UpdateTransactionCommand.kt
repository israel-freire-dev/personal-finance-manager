package tech.freire.dev.personal_finance_manager.application.request

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class UpdateTransactionCommand(
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val status: TransactionStatus,
    val categoryId: UUID
)
