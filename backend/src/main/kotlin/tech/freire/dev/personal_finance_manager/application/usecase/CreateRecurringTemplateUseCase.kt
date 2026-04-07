package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateRecurringTemplateCommand
import tech.freire.dev.personal_finance_manager.application.response.RecurringTemplateResponse
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.model.RecurringTemplate
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.RecurringTemplateRepository
import java.time.LocalDateTime
import java.util.UUID

class CreateRecurringTemplateUseCase(
    private val recurringTemplateRepository: RecurringTemplateRepository,
    private val categoryRepository: CategoryRepository
) : CreateRecurringTemplateInputPort {

    override fun execute(command: CreateRecurringTemplateCommand): RecurringTemplateResponse {
        val category = categoryRepository.findById(command.categoryId)
            ?: throw DomainException("Category not found with id: ${command.categoryId}")

        if (category.userId != command.userId) {
            throw DomainException("Category belongs to another user")
        }

        val endDate = command.endDate ?: command.totalInstallments?.let {
            // Se tiver totalInstallments, calcular endDate baseado na frequency
            // Por enquanto, um fallback simplificado (ideal seria calcular exatamente)
            when (command.frequency) {
                tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency.MONTHLY -> command.startDate.plusMonths(it.toLong() - 1)
                tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency.ANNUAL -> command.startDate.plusYears(it.toLong() - 1)
                tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency.WEEKLY -> command.startDate.plusWeeks(it.toLong() - 1)
            }
        }
        
        val template = RecurringTemplate(
            id = UUID.randomUUID(),
            userId = command.userId,
            categoryId = command.categoryId,
            description = command.description,
            amount = command.amount,
            frequency = command.frequency,
            startDate = command.startDate,
            endDate = endDate,
            totalInstallments = command.totalInstallments,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            lastGeneratedDate = null
        )

        val saved = recurringTemplateRepository.save(template)
        return RecurringTemplateResponse.fromDomain(saved)
    }
}
