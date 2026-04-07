package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import tech.freire.dev.personal_finance_manager.infrastructure.persistence.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Spring Data JPA repository para UserEntity.
 * Camada de framework — apenas delega queries ao Spring Data.
 */
interface SpringUserJpaRepository : JpaRepository<UserEntity, UUID>
