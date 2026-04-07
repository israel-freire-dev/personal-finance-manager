package tech.freire.dev.personal_finance_manager.application.response

import tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency
import tech.freire.dev.personal_finance_manager.domain.model.RecurringTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class RecurringTemplateResponse(
    val id: String,
    val userId: String,
    val categoryId: String,
    val description: String,
    val amount: BigDecimal,
    val frequency: RecurringFrequency,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val totalInstallments: Int?,
    val lastGeneratedDate: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(template: RecurringTemplate): RecurringTemplateResponse {
            return RecurringTemplateResponse(
                id = template.id.toString(),
                userId = template.userId.toString(),
                categoryId = template.categoryId.toString(),
                description = template.description,
                amount = template.amount,
                frequency = template.frequency,
                startDate = template.startDate,
                endDate = template.endDate,
                totalInstallments = template.totalInstallments,
                lastGeneratedDate = template.lastGeneratedDate,
                createdAt = template.createdAt,
                updatedAt = template.updatedAt
            )
        }
    }
}
