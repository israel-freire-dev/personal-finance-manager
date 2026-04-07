package tech.freire.dev.personal_finance_manager.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

import tech.freire.dev.personal_finance_manager.domain.model.DomainException

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
) {
    init {
        if (month !in 1..12) {
            throw DomainException("Month must be between 1 and 12")
        }
        if (year <= 0) {
            throw DomainException("Year must be greater than zero")
        }
        
        val expectedClosingBalance = openingBalance + totalIncome - totalExpenses
        // using compareTo as dealing with BigDecimal
        if (closingBalance.compareTo(expectedClosingBalance) != 0) {
            throw DomainException("Closing balance must equal opening balance + total income - total expenses")
        }
    }
}
