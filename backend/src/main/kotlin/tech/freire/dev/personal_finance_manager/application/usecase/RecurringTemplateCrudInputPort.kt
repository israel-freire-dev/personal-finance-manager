package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.UpdateRecurringTemplateCommand
import tech.freire.dev.personal_finance_manager.application.response.RecurringTemplateResponse
import java.util.UUID

interface RecurringTemplateCrudInputPort {
    fun findById(id: UUID): RecurringTemplateResponse?
    fun findAllByUserId(userId: UUID): List<RecurringTemplateResponse>
    fun update(id: UUID, command: UpdateRecurringTemplateCommand): RecurringTemplateResponse
    // Delete already exists in DeleteRecurringTemplateUseCase
    // Create already exists in CreateRecurringTemplateInputPort
}
