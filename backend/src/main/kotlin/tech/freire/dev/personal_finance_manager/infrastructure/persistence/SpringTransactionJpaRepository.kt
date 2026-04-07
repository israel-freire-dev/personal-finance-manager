package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import tech.freire.dev.personal_finance_manager.infrastructure.persistence.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Spring Data JPA repository para TransactionEntity.
 * Camada de framework — apenas delega queries ao Spring Data.
 */
interface SpringTransactionJpaRepository : JpaRepository<TransactionEntity, UUID>
