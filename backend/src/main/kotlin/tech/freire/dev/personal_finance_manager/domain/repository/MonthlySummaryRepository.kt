package tech.freire.dev.personal_finance_manager.domain.repository

import tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary
import java.util.UUID

interface MonthlySummaryRepository {
    fun save(summary: MonthlySummary): MonthlySummary
    fun findById(id: UUID): MonthlySummary?
    fun findByUserIdAndMonthYear(userId: UUID, month: Int, year: Int): MonthlySummary?
    fun findAllByUserId(userId: UUID): List<MonthlySummary>
}
