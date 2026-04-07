package tech.freire.dev.personal_finance_manager.application.usecase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary
import tech.freire.dev.personal_finance_manager.domain.repository.MonthlySummaryRepository
import java.math.BigDecimal
import java.util.UUID

@DisplayName("GetMonthlySummaryUseCase")
class GetMonthlySummaryUseCaseTest {

    private lateinit var monthlySummaryRepository: MonthlySummaryRepository
    private lateinit var useCase: GetMonthlySummaryUseCase

    private val userId = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        monthlySummaryRepository = mock()
        useCase = GetMonthlySummaryUseCase(monthlySummaryRepository)
    }

    @Nested
    @DisplayName("Cenários de consulta")
    inner class QueryScenarios {

        @Test
        @DisplayName("Deve retornar summary existente sem criar novo")
        fun shouldReturnExistingSummary() {
            val existing = MonthlySummary.createEmpty(userId, 4, 2026, BigDecimal("500.00"))
                .applyTransaction(BigDecimal("100.00"), TransactionType.EXPENSE, TransactionStatus.PAID)
            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 4, 2026)).thenReturn(existing)

            val response = useCase.execute(userId, 4, 2026)

            assertEquals(4, response.month)
            assertEquals(2026, response.year)
            assertEquals(BigDecimal("500.00"), response.openingBalance)
            assertEquals(BigDecimal("100.00"), response.totalExpenses)
            assertEquals(BigDecimal("400.00"), response.closingBalance)
            verify(monthlySummaryRepository, never()).save(any())
        }

        @Test
        @DisplayName("Deve criar summary com openingBalance ZERO quando não há mês anterior")
        fun shouldCreateWithZeroWhenNoPreviousMonth() {
            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 4, 2026)).thenReturn(null)
            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 3, 2026)).thenReturn(null)
            whenever(monthlySummaryRepository.save(any())).thenAnswer { it.getArgument<MonthlySummary>(0) }

            val response = useCase.execute(userId, 4, 2026)

            assertEquals(4, response.month)
            assertEquals(2026, response.year)
            assertEquals(BigDecimal.ZERO, response.openingBalance)
            assertEquals(BigDecimal.ZERO, response.closingBalance)
            verify(monthlySummaryRepository, times(1)).save(any())
        }

        @Test
        @DisplayName("Deve criar summary com rollover do mês anterior")
        fun shouldCreateWithRolloverFromPreviousMonth() {
            val previousSummary = MonthlySummary.createEmpty(userId, 3, 2026, BigDecimal("1000.00"))
                .applyTransaction(BigDecimal("300.00"), TransactionType.INCOME, TransactionStatus.PAID)
            // closingBalance = 1000 + 300 = 1300

            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 4, 2026)).thenReturn(null)
            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 3, 2026)).thenReturn(previousSummary)
            whenever(monthlySummaryRepository.save(any())).thenAnswer { it.getArgument<MonthlySummary>(0) }

            val response = useCase.execute(userId, 4, 2026)

            assertEquals(BigDecimal("1300.00"), response.openingBalance)
            assertEquals(BigDecimal("1300.00"), response.closingBalance)
            assertEquals(BigDecimal("1300.00"), response.projectedBalance)
        }

        @Test
        @DisplayName("Rollover de janeiro deve buscar dezembro do ano anterior")
        fun shouldRolloverJanuaryFromDecember() {
            val decemberSummary = MonthlySummary.createEmpty(userId, 12, 2025, BigDecimal("2000.00"))
            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 1, 2026)).thenReturn(null)
            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 12, 2025)).thenReturn(decemberSummary)
            whenever(monthlySummaryRepository.save(any())).thenAnswer { it.getArgument<MonthlySummary>(0) }

            val response = useCase.execute(userId, 1, 2026)

            assertEquals(BigDecimal("2000.00"), response.openingBalance)
        }
    }

    @Nested
    @DisplayName("Cenários de projectedBalance")
    inner class ProjectedBalanceScenarios {

        @Test
        @DisplayName("projectedBalance deve considerar pendentes")
        fun shouldCalculateProjectedBalance() {
            val summary = MonthlySummary.createEmpty(userId, 4, 2026, BigDecimal("1000.00"))
                .applyTransaction(BigDecimal("200.00"), TransactionType.EXPENSE, TransactionStatus.PAID)
                .applyTransaction(BigDecimal("500.00"), TransactionType.EXPENSE, TransactionStatus.PENDING)
                .applyTransaction(BigDecimal("100.00"), TransactionType.INCOME, TransactionStatus.PENDING)
            // closingBalance = 1000 + 0 - 200 = 800
            // projectedBalance = 800 + 100 - 500 = 400

            whenever(monthlySummaryRepository.findByUserIdAndMonthYear(userId, 4, 2026)).thenReturn(summary)

            val response = useCase.execute(userId, 4, 2026)

            assertEquals(BigDecimal("800.00"), response.closingBalance)
            assertEquals(BigDecimal("400.00"), response.projectedBalance)
        }
    }
}
