package tech.freire.dev.personal_finance_manager.adapter.outbound.persistence

import tech.freire.dev.personal_finance_manager.dto.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Spring Data JPA repository para UserEntity.
 * Camada de framework — apenas delega queries ao Spring Data.
 */
interface SpringUserJpaRepository : JpaRepository<UserEntity, UUID>
