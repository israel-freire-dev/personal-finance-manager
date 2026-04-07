package tech.freire.dev.personal_finance_manager.application.usecase

import java.time.LocalDate

interface ProcessRecurringTransactionsInputPort {
    fun execute(referenceDate: LocalDate = LocalDate.now())
}
