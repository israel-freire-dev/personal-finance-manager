package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface SpringRecurringTemplateJpaRepository : JpaRepository<RecurringTemplateEntity, UUID> {
    fun findAllByUserId(userId: UUID): List<RecurringTemplateEntity>

    @Query("SELECT t FROM RecurringTemplateEntity t WHERE t.endDate IS NULL OR t.endDate >= CURRENT_DATE")
    fun findAllActiveTemplates(): List<RecurringTemplateEntity>
}
