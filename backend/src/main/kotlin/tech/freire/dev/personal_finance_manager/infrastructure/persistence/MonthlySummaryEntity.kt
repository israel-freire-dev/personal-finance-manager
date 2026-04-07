package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "monthly_summaries")
data class MonthlySummaryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Column(nullable = false)
    val month: Int,

    @Column(nullable = false)
    val year: Int,

    @Column(name = "opening_balance", nullable = false, precision = 19, scale = 4)
    val openingBalance: BigDecimal,

    @Column(name = "total_income", nullable = false, precision = 19, scale = 4)
    val totalIncome: BigDecimal,

    @Column(name = "total_expenses", nullable = false, precision = 19, scale = 4)
    val totalExpenses: BigDecimal,

    @Column(name = "total_income_pending", nullable = false, precision = 19, scale = 4)
    val totalIncomePending: BigDecimal = BigDecimal.ZERO,

    @Column(name = "total_expenses_pending", nullable = false, precision = 19, scale = 4)
    val totalExpensesPending: BigDecimal = BigDecimal.ZERO,

    @Column(name = "closing_balance", nullable = false, precision = 19, scale = 4)
    val closingBalance: BigDecimal,

    @Column(name = "is_closed", nullable = false)
    val isClosed: Boolean
) : BaseEntity() {

    fun toDomain(): tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary {
        return tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary(
            id = id,
            userId = user.id,
            month = month,
            year = year,
            openingBalance = openingBalance,
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            totalIncomePending = totalIncomePending,
            totalExpensesPending = totalExpensesPending,
            closingBalance = closingBalance,
            isClosed = isClosed,
            createdAt = createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = updatedAt ?: java.time.LocalDateTime.now()
        )
    }

    companion object {
        fun fromDomain(
            summary: tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary,
            user: UserEntity
        ): MonthlySummaryEntity {
            return MonthlySummaryEntity(
                id = summary.id,
                user = user,
                month = summary.month,
                year = summary.year,
                openingBalance = summary.openingBalance,
                totalIncome = summary.totalIncome,
                totalExpenses = summary.totalExpenses,
                totalIncomePending = summary.totalIncomePending,
                totalExpensesPending = summary.totalExpensesPending,
                closingBalance = summary.closingBalance,
                isClosed = summary.isClosed
            )
        }
    }
}
