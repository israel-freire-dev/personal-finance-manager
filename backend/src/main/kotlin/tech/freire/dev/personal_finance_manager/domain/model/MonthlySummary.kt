package tech.freire.dev.personal_finance_manager.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class MonthlySummary(
    val id: UUID,
    val userId: UUID,
    val month: Int,
    val year: Int,
    val openingBalance: BigDecimal,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val closingBalance: BigDecimal,
    val isClosed: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
