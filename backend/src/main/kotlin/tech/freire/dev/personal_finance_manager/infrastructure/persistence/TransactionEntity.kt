package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import jakarta.persistence.*
import jakarta.validation.constraints.Positive
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.Transaction
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
    
    @Positive
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
) : BaseEntity() {

    /**
     * Converte entidade JPA para entidade de domínio.
     */
    fun toDomain(): Transaction {
        return Transaction(
            id = id,
            userId = user.id,
            categoryId = category.id,
            recurringTemplateId = recurringTemplate?.id,
            description = description,
            amount = amount,
            date = date,
            status = status,
            type = type,
            createdAt = createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = updatedAt ?: java.time.LocalDateTime.now()
        )
    }

    companion object {
        /**
         * Cria entidade JPA a partir da entidade de domínio.
         * Requer as entidades JPA relacionadas pré-carregadas.
         */
        fun fromDomain(
            transaction: Transaction,
            user: UserEntity,
            category: CategoryEntity,
            recurringTemplate: RecurringTemplateEntity?
        ): TransactionEntity {
            return TransactionEntity(
                id = transaction.id,
                user = user,
                category = category,
                recurringTemplate = recurringTemplate,
                description = transaction.description,
                amount = transaction.amount,
                date = transaction.date,
                status = transaction.status,
                type = transaction.type
            )
        }
    }
}
