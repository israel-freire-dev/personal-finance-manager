package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringMonthlySummaryJpaRepository : JpaRepository<MonthlySummaryEntity, UUID> {
    fun findByUserIdAndMonthAndYear(userId: UUID, month: Int, year: Int): MonthlySummaryEntity?
    fun findAllByUserId(userId: UUID): List<MonthlySummaryEntity>
}
