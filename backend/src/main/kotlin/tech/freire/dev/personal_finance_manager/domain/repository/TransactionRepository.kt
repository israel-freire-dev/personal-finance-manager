package tech.freire.dev.personal_finance_manager.domain.repository

import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import java.time.LocalDate
import java.util.UUID

interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findById(id: UUID): Transaction?
    fun findAllByUserId(userId: UUID): List<Transaction>
    fun findByUserIdAndDateRange(userId: UUID, from: LocalDate, to: LocalDate): List<Transaction>
    fun delete(id: UUID)
}
