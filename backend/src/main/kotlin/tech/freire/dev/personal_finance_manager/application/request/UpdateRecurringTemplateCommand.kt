package tech.freire.dev.personal_finance_manager.application.request

import java.math.BigDecimal
import java.time.LocalDate

data class UpdateRecurringTemplateCommand(
    val description: String,
    val amount: BigDecimal,
    val endDate: LocalDate?
)
