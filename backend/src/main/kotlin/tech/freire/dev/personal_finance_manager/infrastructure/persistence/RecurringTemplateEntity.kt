package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import jakarta.persistence.*
import tech.freire.dev.personal_finance_manager.domain.enums.RecurringFrequency
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "recurring_templates")
data class RecurringTemplateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    val category: CategoryEntity,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false, precision = 19, scale = 4)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val frequency: RecurringFrequency,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date")
    val endDate: LocalDate? = null,

    @Column(name = "total_installments")
    val totalInstallments: Int? = null,

    @Column(name = "last_generated_date")
    val lastGeneratedDate: LocalDate? = null
) : BaseEntity() {

    fun toDomain(): tech.freire.dev.personal_finance_manager.domain.model.RecurringTemplate {
        return tech.freire.dev.personal_finance_manager.domain.model.RecurringTemplate(
            id = id,
            userId = user.id,
            categoryId = category.id,
            description = description,
            amount = amount,
            frequency = frequency,
            startDate = startDate,
            endDate = endDate,
            totalInstallments = totalInstallments,
            lastGeneratedDate = lastGeneratedDate,
            createdAt = createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = updatedAt ?: java.time.LocalDateTime.now()
        )
    }
}
