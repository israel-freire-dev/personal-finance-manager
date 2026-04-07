package tech.freire.dev.personal_finance_manager.application.response

import tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary
import java.math.BigDecimal

/**
 * DTO de saída com o resumo financeiro mensal.
 * Contém saldo real (closingBalance) e saldo previsto (projectedBalance).
 */
data class MonthlySummaryResponse(
    val id: String,
    val userId: String,
    val month: Int,
    val year: Int,
    val openingBalance: BigDecimal,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val closingBalance: BigDecimal,
    val totalIncomePending: BigDecimal,
    val totalExpensesPending: BigDecimal,
    val projectedBalance: BigDecimal,
    val isClosed: Boolean
) {
    companion object {
        fun fromDomain(summary: MonthlySummary): MonthlySummaryResponse {
            return MonthlySummaryResponse(
                id = summary.id.toString(),
                userId = summary.userId.toString(),
                month = summary.month,
                year = summary.year,
                openingBalance = summary.openingBalance,
                totalIncome = summary.totalIncome,
                totalExpenses = summary.totalExpenses,
                closingBalance = summary.closingBalance,
                totalIncomePending = summary.totalIncomePending,
                totalExpensesPending = summary.totalExpensesPending,
                projectedBalance = summary.projectedBalance,
                isClosed = summary.isClosed
            )
        }
    }
}
