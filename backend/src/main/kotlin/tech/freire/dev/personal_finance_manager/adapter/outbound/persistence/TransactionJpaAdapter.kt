package tech.freire.dev.personal_finance_manager.adapter.outbound.persistence

import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import tech.freire.dev.personal_finance_manager.dto.TransactionEntity
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

/**
 * Adapter que implementa o port de repositório de transações do domínio.
 * Traduz entre entidades de domínio e entidades JPA.
 */
@Component
class TransactionJpaAdapter(
    private val jpaRepository: SpringTransactionJpaRepository,
    private val userJpaRepository: SpringUserJpaRepository,
    private val categoryJpaRepository: SpringCategoryJpaRepository
) : TransactionRepository {

    override fun save(transaction: Transaction): Transaction {
        val userEntity = userJpaRepository.findById(transaction.userId)
            .orElseThrow { IllegalStateException("User not found: ${transaction.userId}") }

        val categoryEntity = categoryJpaRepository.findById(transaction.categoryId)
            .orElseThrow { IllegalStateException("Category not found: ${transaction.categoryId}") }

        val entity = TransactionEntity.fromDomain(
            transaction = transaction,
            user = userEntity,
            category = categoryEntity,
            recurringTemplate = null
        )

        return jpaRepository.save(entity).toDomain()
    }

    override fun findById(id: UUID): Transaction? {
        return jpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAllByUserId(userId: UUID): List<Transaction> {
        return jpaRepository.findAll()
            .filter { it.user.id == userId }
            .map { it.toDomain() }
    }

    override fun findByUserIdAndDateRange(userId: UUID, from: LocalDate, to: LocalDate): List<Transaction> {
        return jpaRepository.findAll()
            .filter { it.user.id == userId && !it.date.isBefore(from) && !it.date.isAfter(to) }
            .map { it.toDomain() }
    }

    override fun delete(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
