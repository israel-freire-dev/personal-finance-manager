package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.response.MonthlySummaryResponse
import tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary
import tech.freire.dev.personal_finance_manager.domain.repository.MonthlySummaryRepository
import java.util.UUID

/**
 * Use Case: consultar o resumo financeiro de um mês.
 *
 * Rollover: Se o summary do mês solicitado não existir, ele é criado
 * com o openingBalance = closingBalance do mês anterior (ou ZERO se for o primeiro mês).
 */
class GetMonthlySummaryUseCase(
    private val monthlySummaryRepository: MonthlySummaryRepository
) : GetMonthlySummaryInputPort {

    override fun execute(userId: UUID, month: Int, year: Int): MonthlySummaryResponse {
        val summary = findOrCreate(userId, month, year)
        return MonthlySummaryResponse.fromDomain(summary)
    }

    /**
     * Busca o summary do mês/ano ou cria um novo com rollover do mês anterior.
     */
    internal fun findOrCreate(userId: UUID, month: Int, year: Int): MonthlySummary {
        val existing = monthlySummaryRepository.findByUserIdAndMonthYear(userId, month, year)
        if (existing != null) return existing

        // Rollover: buscar closingBalance do mês anterior
        val (prevMonth, prevYear) = if (month == 1) Pair(12, year - 1) else Pair(month - 1, year)
        val previousSummary = monthlySummaryRepository.findByUserIdAndMonthYear(userId, prevMonth, prevYear)
        val openingBalance = previousSummary?.closingBalance ?: java.math.BigDecimal.ZERO

        val newSummary = MonthlySummary.createEmpty(
            userId = userId,
            month = month,
            year = year,
            openingBalance = openingBalance
        )

        return monthlySummaryRepository.save(newSummary)
    }
}
