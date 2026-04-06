package tech.freire.dev.personal_finance_manager.dto

import jakarta.persistence.*
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    val category: CategoryEntity,

    @ManyToOne
    @JoinColumn(name = "recurring_template_id")
    val recurringTemplate: RecurringTemplateEntity?,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false, precision = 19, scale = 4)
    val amount: BigDecimal,

    @Column(nullable = false)
    val date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: TransactionStatus,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType
) : BaseEntity()
