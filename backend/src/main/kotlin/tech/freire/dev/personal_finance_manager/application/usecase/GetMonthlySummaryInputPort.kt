package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.response.MonthlySummaryResponse
import java.util.UUID

interface GetMonthlySummaryInputPort {
    fun execute(userId: UUID, month: Int, year: Int): MonthlySummaryResponse
}
