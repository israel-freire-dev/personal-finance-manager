package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import tech.freire.dev.personal_finance_manager.domain.repository.RecurringTemplateRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class ProcessRecurringTransactionsUseCase(
    private val recurringTemplateRepository: RecurringTemplateRepository,
    private val transactionRepository: TransactionRepository
) : ProcessRecurringTransactionsInputPort {

    override fun execute(referenceDate: LocalDate) {
        // Encontra os templates que ainda não venceram e não possuem lastGeneratedDate no mês alvo
        val activeTemplates = recurringTemplateRepository.findAllActiveTemplates()

        val nextMonth = referenceDate.plusMonths(1)
        val monthTarget = nextMonth.monthValue
        val yearTarget = nextMonth.year

        for (template in activeTemplates) {
            // Checar se já rodou
            if (template.lastGeneratedDate != null && 
                template.lastGeneratedDate.monthValue == monthTarget && 
                template.lastGeneratedDate.year == yearTarget) {
                continue
            }

            // Gerar transação para o próximo mês
            var newDate = template.startDate.withMonth(monthTarget).withYear(yearTarget) // Simplificado (ex: todo dia 15). Cuidado com Fevereiro/29!
            // Correção para dia 31 em meses de 30 etc:
            val maxDay = newDate.lengthOfMonth()
            if (template.startDate.dayOfMonth > maxDay) {
                newDate = newDate.withDayOfMonth(maxDay)
            } else {
                newDate = newDate.withDayOfMonth(template.startDate.dayOfMonth)
            }

            // Se for infinito, ou ainda estivermos antes da data final
            if (template.endDate == null || !newDate.isAfter(template.endDate)) {
                val newTransaction = Transaction(
                    id = UUID.randomUUID(),
                    userId = template.userId,
                    categoryId = template.categoryId,
                    recurringTemplateId = template.id,
                    description = template.description,
                    amount = template.amount,
                    date = newDate,
                    status = TransactionStatus.PENDING, // Por padrão, lança pendente no mês seguinte
                    type = tech.freire.dev.personal_finance_manager.domain.enums.TransactionType.EXPENSE, // Assumindo despesa, ou pegar do Category
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )

                transactionRepository.save(newTransaction)

                // Atualizar template para marcar que o mês alvo foi processado
                val updatedTemplate = template.copy(
                    lastGeneratedDate = LocalDate.of(yearTarget, monthTarget, 1)
                )
                recurringTemplateRepository.save(updatedTemplate)
            }
        }
    }
}
