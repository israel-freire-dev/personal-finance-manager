package tech.freire.dev.personal_finance_manager.dto

import jakarta.persistence.*
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.Category
import java.util.UUID

@Entity
@Table(name = "categories")
data class CategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne
    @JoinColumn(name = "parent_id")
    val parent: CategoryEntity?,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,

    @Column(length = 7)
    val color: String?,

    val icon: String?
) : BaseEntity() {

    /**
     * Converte entidade JPA para entidade de domínio.
     */
    fun toDomain(): Category {
        return Category(
            id = id,
            userId = user.id,
            parentId = parent?.id,
            name = name,
            type = type,
            color = color,
            icon = icon,
            createdAt = createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = updatedAt ?: java.time.LocalDateTime.now()
        )
    }
}
