package tech.freire.dev.personal_finance_manager.application.dto

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * DTO de saída com os dados da transação criada.
 * Retornado pelo use case para o controller.
 */
data class TransactionResponse(
    val id: String,
    val userId: String,
    val categoryId: String,
    val description: String,
    val amount: BigDecimal,
    val date: LocalDate,
    val status: TransactionStatus,
    val type: TransactionType,
    val recurringTemplateId: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(transaction: Transaction): TransactionResponse {
            return TransactionResponse(
                id = transaction.id.toString(),
                userId = transaction.userId.toString(),
                categoryId = transaction.categoryId.toString(),
                description = transaction.description,
                amount = transaction.amount,
                date = transaction.date,
                status = transaction.status,
                type = transaction.type,
                recurringTemplateId = transaction.recurringTemplateId?.toString(),
                createdAt = transaction.createdAt,
                updatedAt = transaction.updatedAt
            )
        }
    }
}
