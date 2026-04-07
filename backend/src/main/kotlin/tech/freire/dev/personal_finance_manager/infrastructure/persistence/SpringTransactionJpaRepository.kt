package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import tech.freire.dev.personal_finance_manager.infrastructure.persistence.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.Modifying
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import java.time.LocalDate

/**
 * Spring Data JPA repository para TransactionEntity.
 * Camada de framework — apenas delega queries ao Spring Data.
 */
interface SpringTransactionJpaRepository : JpaRepository<TransactionEntity, UUID> {
    fun findAllByUserId(userId: UUID): List<TransactionEntity>
    fun findAllByUserIdAndDateBetween(userId: UUID, from: LocalDate, to: LocalDate): List<TransactionEntity>
    
    @Modifying
    @Query("DELETE FROM TransactionEntity t WHERE t.recurringTemplate.id = :templateId AND t.status = :status AND t.date > :date")
    fun deleteByRecurringTemplateIdAndStatusAndDateAfter(templateId: UUID, status: TransactionStatus, date: LocalDate): Int
}
