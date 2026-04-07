package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.UpdateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.response.TransactionResponse
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary
import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.MonthlySummaryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class TransactionCrudUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val monthlySummaryRepository: MonthlySummaryRepository
) : TransactionCrudInputPort {

    override fun findById(id: UUID): TransactionResponse? {
        val transaction = transactionRepository.findById(id) ?: return null
        return TransactionResponse.fromDomain(transaction)
    }

    override fun findAllByUserId(userId: UUID): List<TransactionResponse> {
        return transactionRepository.findAllByUserId(userId).map { TransactionResponse.fromDomain(it) }
    }

    override fun update(id: UUID, command: UpdateTransactionCommand): TransactionResponse {
        val transaction = transactionRepository.findById(id)
            ?: throw DomainException("Transaction not found with id: $id")

        // Validações da categoria
        val category = categoryRepository.findById(command.categoryId)
            ?: throw DomainException("Category not found with id: ${command.categoryId}")

        if (category.userId != transaction.userId) {
            throw DomainException("Category belongs to another user")
        }

        // Se a data, status ou tipo mudou, precisamos recalcular os summaries do mês antigo e do novo mês
        val isMonthChanged = transaction.date.monthValue != command.date.monthValue || transaction.date.year != command.date.year
        val isImpactChanged = transaction.status != command.status || transaction.amount.compareTo(command.amount) != 0 || transaction.type != category.type

        if (isMonthChanged || isImpactChanged) {
            revertSummaryImpact(transaction)
        }

        val updatedTransaction = transaction.copy(
            description = command.description,
            amount = command.amount,
            date = command.date,
            status = command.status,
            type = category.type,
            categoryId = command.categoryId,
            updatedAt = LocalDateTime.now()
        )

        val saved = transactionRepository.save(updatedTransaction)

        if (isMonthChanged || isImpactChanged) {
            applySummaryImpact(saved)
        }

        return TransactionResponse.fromDomain(saved)
    }

    override fun delete(id: UUID) {
        val transaction = transactionRepository.findById(id)
            ?: throw DomainException("Transaction not found with id: $id")

        revertSummaryImpact(transaction)
        transactionRepository.delete(id)
    }

    private fun applySummaryImpact(transaction: Transaction) {
        val summary = monthlySummaryRepository.findByUserIdAndMonthYear(
            transaction.userId, transaction.date.monthValue, transaction.date.year
        ) ?: createSummaryWithRollover(transaction.userId, transaction.date.monthValue, transaction.date.year)

        val updated = summary.applyTransaction(transaction.amount, transaction.type, transaction.status)
        monthlySummaryRepository.save(updated)
    }

    private fun revertSummaryImpact(transaction: Transaction) {
        val summary = monthlySummaryRepository.findByUserIdAndMonthYear(
            transaction.userId, transaction.date.monthValue, transaction.date.year
        ) ?: return

        // Inverser o impacto (amount negativo)
        // Isso assume que applyTransaction suporta ser chamado com negativo, mas nosso applyTransaction PODE nao suportar (se BigDecimal não permitir ou a logica for rígida).
        // Na verdade `applyTransaction` apenas soma, então se passarmos amount negativo ele subtrai. Como BigDecimal aceita negativo, vai funcionar perfeitamente, já que `value + (-amount) = value - amount`.
        // A Exceção no transaction é lançada APÓS validação no domínio se amount <= 0, ENTÃO `applyTransaction` que pega o amount pode receber negativo? `applyTransaction` NAO TEM restrição no amount em si (MonthlySummary.kt nao valida amount em si).
        val reverted = summary.applyTransaction(transaction.amount.negate(), transaction.type, transaction.status)
        monthlySummaryRepository.save(reverted)
    }

    private fun createSummaryWithRollover(userId: UUID, month: Int, year: Int): MonthlySummary {
        val (prevMonth, prevYear) = if (month == 1) Pair(12, year - 1) else Pair(month - 1, year)
        val previousSummary = monthlySummaryRepository.findByUserIdAndMonthYear(userId, prevMonth, prevYear)
        val openingBalance = previousSummary?.closingBalance ?: BigDecimal.ZERO

        return MonthlySummary.createEmpty(userId, month, year, openingBalance)
    }
}
