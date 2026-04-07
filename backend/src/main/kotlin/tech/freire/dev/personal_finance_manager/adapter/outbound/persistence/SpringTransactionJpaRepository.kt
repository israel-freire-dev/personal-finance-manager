package tech.freire.dev.personal_finance_manager.adapter.outbound.persistence

import tech.freire.dev.personal_finance_manager.dto.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Spring Data JPA repository para TransactionEntity.
 * Camada de framework — apenas delega queries ao Spring Data.
 */
interface SpringTransactionJpaRepository : JpaRepository<TransactionEntity, UUID>
