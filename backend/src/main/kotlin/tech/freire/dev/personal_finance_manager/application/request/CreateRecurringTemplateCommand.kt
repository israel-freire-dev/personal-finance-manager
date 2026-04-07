package tech.freire.dev.personal_finance_manager.application.request

import tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class CreateRecurringTemplateCommand(
    val userId: UUID,
    val categoryId: UUID,
    val description: String,
    val amount: BigDecimal,
    val frequency: RecurringFrequency,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val totalInstallments: Int?
)
