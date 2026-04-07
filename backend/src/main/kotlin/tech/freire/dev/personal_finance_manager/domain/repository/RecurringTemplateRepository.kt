package tech.freire.dev.personal_finance_manager.domain.repository

import tech.freire.dev.personal_finance_manager.domain.model.RecurringTemplate
import java.time.LocalDate
import java.util.UUID

interface RecurringTemplateRepository {
    fun save(template: RecurringTemplate): RecurringTemplate
    fun findById(id: UUID): RecurringTemplate?
    fun findAllByUserId(userId: UUID): List<RecurringTemplate>
    fun findActiveByUserId(userId: UUID, referenceDate: LocalDate): List<RecurringTemplate>
    fun findAllActiveTemplates(): List<RecurringTemplate>
    fun delete(id: UUID)
}
