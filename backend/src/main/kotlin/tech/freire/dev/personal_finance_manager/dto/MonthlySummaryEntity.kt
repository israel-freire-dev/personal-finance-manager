package tech.freire.dev.personal_finance_manager.dto

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

    @Column(name = "closing_balance", nullable = false, precision = 19, scale = 4)
    val closingBalance: BigDecimal,

    @Column(name = "is_closed", nullable = false)
    val isClosed: Boolean
) : BaseEntity()
