package tech.freire.dev.personal_finance_manager.domain.model

import tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RecurringTemplate(
    val id: UUID,
    val userId: UUID,
    val categoryId: UUID,
    val description: String,
    val amount: BigDecimal,
    val frequency: RecurringFrequency,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val totalInstallments: Int?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
