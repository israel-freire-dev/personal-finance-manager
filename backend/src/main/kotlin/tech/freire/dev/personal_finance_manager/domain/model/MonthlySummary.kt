package tech.freire.dev.personal_finance_manager.domain.model

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Entidade de domínio: resumo financeiro mensal de um usuário.
 *
 * Campos "reais" (efetivados):
 *   totalIncome / totalExpenses → transações com status PAID ou INVESTED
 *   closingBalance = openingBalance + totalIncome − totalExpenses
 *
 * Campos "previstos" (pendentes):
 *   totalIncomePending / totalExpensesPending → transações com status PENDING
 *   projectedBalance = closingBalance + totalIncomePending − totalExpensesPending
 *
 * REFUNDED em EXPENSE → o dinheiro voltou, então NÃO soma em despesas reais.
 * CANCELLED → não afeta nenhum saldo.
 */
data class MonthlySummary(
    val id: UUID,
    val userId: UUID,
    val month: Int,
    val year: Int,
    val openingBalance: BigDecimal,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val totalIncomePending: BigDecimal,
    val totalExpensesPending: BigDecimal,
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
        if (closingBalance.compareTo(expectedClosingBalance) != 0) {
            throw DomainException("Closing balance must equal opening balance + total income - total expenses")
        }
    }

    /**
     * Saldo previsto = saldo real + receitas pendentes − despesas pendentes.
     */
    val projectedBalance: BigDecimal
        get() = closingBalance + totalIncomePending - totalExpensesPending

    /**
     * Aplica o impacto de uma nova transação e retorna uma cópia atualizada.
     *
     * Regras:
     *   CANCELLED / REFUNDED  → sem impacto
     *   PENDING + INCOME      → incrementa totalIncomePending
     *   PENDING + EXPENSE     → incrementa totalExpensesPending
     *   PAID|INVESTED + INCOME  → incrementa totalIncome, recalcula closingBalance
     *   PAID|INVESTED + EXPENSE → incrementa totalExpenses, recalcula closingBalance
     */
    fun applyTransaction(
        amount: BigDecimal,
        type: TransactionType,
        status: TransactionStatus
    ): MonthlySummary {
        return when (status) {
            TransactionStatus.CANCELLED, TransactionStatus.REFUNDED -> this.copy(updatedAt = LocalDateTime.now())

            TransactionStatus.PENDING -> when (type) {
                TransactionType.INCOME -> copy(
                    totalIncomePending = totalIncomePending + amount,
                    updatedAt = LocalDateTime.now()
                )
                TransactionType.EXPENSE -> copy(
                    totalExpensesPending = totalExpensesPending + amount,
                    updatedAt = LocalDateTime.now()
                )
            }

            TransactionStatus.PAID, TransactionStatus.INVESTED -> when (type) {
                TransactionType.INCOME -> {
                    val newIncome = totalIncome + amount
                    copy(
                        totalIncome = newIncome,
                        closingBalance = openingBalance + newIncome - totalExpenses,
                        updatedAt = LocalDateTime.now()
                    )
                }
                TransactionType.EXPENSE -> {
                    val newExpenses = totalExpenses + amount
                    copy(
                        totalExpenses = newExpenses,
                        closingBalance = openingBalance + totalIncome - newExpenses,
                        updatedAt = LocalDateTime.now()
                    )
                }
            }
        }
    }

    companion object {
        /**
         * Cria um summary zerado para um mês/ano, usando o openingBalance fornecido (rollover).
         */
        fun createEmpty(
            userId: UUID,
            month: Int,
            year: Int,
            openingBalance: BigDecimal = BigDecimal.ZERO
        ): MonthlySummary {
            val now = LocalDateTime.now()
            return MonthlySummary(
                id = UUID.randomUUID(),
                userId = userId,
                month = month,
                year = year,
                openingBalance = openingBalance,
                totalIncome = BigDecimal.ZERO,
                totalExpenses = BigDecimal.ZERO,
                totalIncomePending = BigDecimal.ZERO,
                totalExpensesPending = BigDecimal.ZERO,
                closingBalance = openingBalance,
                isClosed = false,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}
