package tech.freire.dev.personal_finance_manager.domain.model

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: UUID,
    val userId: UUID,
    val categoryId: UUID,
    val recurringTemplateId: UUID?,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val status: TransactionStatus,
    val type: TransactionType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
