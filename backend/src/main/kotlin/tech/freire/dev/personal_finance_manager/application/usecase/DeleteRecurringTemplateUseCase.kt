package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.repository.RecurringTemplateRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import java.time.LocalDate
import java.util.UUID

class DeleteRecurringTemplateUseCase(
    private val recurringTemplateRepository: RecurringTemplateRepository,
    private val transactionRepository: TransactionRepository
) {
    fun execute(templateId: UUID) {
        // Apaga do banco o template recorrente
        recurringTemplateRepository.delete(templateId)
        
        // Remove as parcelas futuras que ainda não foram pagas (estão PENDING e a data é pro futuro)
        transactionRepository.deleteByRecurringTemplateIdAndStatusAndDateAfter(
            templateId = templateId,
            status = TransactionStatus.PENDING,
            date = LocalDate.now()
        )
    }
}
