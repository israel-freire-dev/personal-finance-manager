package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.UpdateRecurringTemplateCommand
import tech.freire.dev.personal_finance_manager.application.response.RecurringTemplateResponse
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.repository.RecurringTemplateRepository
import java.time.LocalDateTime
import java.util.UUID

class RecurringTemplateCrudUseCase(
    private val recurringTemplateRepository: RecurringTemplateRepository
) : RecurringTemplateCrudInputPort {

    override fun findById(id: UUID): RecurringTemplateResponse? {
        val template = recurringTemplateRepository.findById(id) ?: return null
        return RecurringTemplateResponse.fromDomain(template)
    }

    override fun findAllByUserId(userId: UUID): List<RecurringTemplateResponse> {
        return recurringTemplateRepository.findAllByUserId(userId)
            .map { RecurringTemplateResponse.fromDomain(it) }
    }

    override fun update(id: UUID, command: UpdateRecurringTemplateCommand): RecurringTemplateResponse {
        val template = recurringTemplateRepository.findById(id)
            ?: throw DomainException("Recurring template not found with id: $id")

        val updatedTemplate = template.copy(
            description = command.description,
            amount = command.amount,
            endDate = command.endDate,
            updatedAt = LocalDateTime.now()
        )

        val saved = recurringTemplateRepository.save(updatedTemplate)
        return RecurringTemplateResponse.fromDomain(saved)
    }
}
