package tech.freire.dev.personal_finance_manager.domain.model

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import java.time.LocalDateTime
import java.util.UUID

data class Category(
    val id: UUID,
    val userId: UUID,
    val parentId: UUID?,
    val name: String,
    val type: TransactionType,
    val color: String?,
    val icon: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
