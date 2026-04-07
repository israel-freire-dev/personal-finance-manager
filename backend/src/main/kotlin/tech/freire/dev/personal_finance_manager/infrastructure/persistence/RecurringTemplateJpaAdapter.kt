package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import org.springframework.stereotype.Component
import tech.freire.dev.personal_finance_manager.domain.model.RecurringTemplate
import tech.freire.dev.personal_finance_manager.domain.repository.RecurringTemplateRepository
import java.time.LocalDate
import java.util.UUID

@Component
class RecurringTemplateJpaAdapter(
    private val jpaRepository: SpringRecurringTemplateJpaRepository,
    private val userRepository: SpringUserJpaRepository,
    private val categoryRepository: SpringCategoryJpaRepository
) : RecurringTemplateRepository {

    override fun save(template: RecurringTemplate): RecurringTemplate {
        // Implementação omitida por brevidade ou fazer o fromDomain/toDomain
        val user = userRepository.findById(template.userId).orElseThrow()
        val cat = categoryRepository.findById(template.categoryId).orElseThrow()
        
        val entity = RecurringTemplateEntity(
            id = template.id,
            user = user,
            category = cat,
            description = template.description,
            amount = template.amount,
            frequency = template.frequency,
            startDate = template.startDate,
            endDate = template.endDate,
            totalInstallments = template.totalInstallments,
            lastGeneratedDate = template.lastGeneratedDate
        )
        return jpaRepository.save(entity).toDomain() // requer .toDomain em entity
    }

    override fun findById(id: UUID): RecurringTemplate? {
        return jpaRepository.findById(id).map { it.toDomain() }.orElse(null)
    }

    override fun findAllByUserId(userId: UUID): List<RecurringTemplate> {
        return jpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }

    override fun findActiveByUserId(userId: UUID, referenceDate: LocalDate): List<RecurringTemplate> {
        return jpaRepository.findAllByUserId(userId).filter { 
            val end = it.endDate
            end == null || end >= referenceDate 
        }.map { it.toDomain() }
    }
    
    override fun findAllActiveTemplates(): List<RecurringTemplate> {
        return jpaRepository.findAllActiveTemplates().map { it.toDomain() }
    }

    override fun delete(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
