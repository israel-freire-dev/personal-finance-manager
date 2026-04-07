package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateRecurringTemplateCommand
import tech.freire.dev.personal_finance_manager.application.response.RecurringTemplateResponse

interface CreateRecurringTemplateInputPort {
    fun execute(command: CreateRecurringTemplateCommand): RecurringTemplateResponse
}
