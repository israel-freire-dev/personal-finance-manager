package tech.freire.dev.personal_finance_manager.application.response

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.Category
import java.time.LocalDateTime

data class CategoryResponse(
    val id: String,
    val userId: String,
    val parentId: String?,
    val name: String,
    val type: TransactionType,
    val color: String?,
    val icon: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(category: Category): CategoryResponse {
            return CategoryResponse(
                id = category.id.toString(),
                userId = category.userId.toString(),
                parentId = category.parentId?.toString(),
                name = category.name,
                type = category.type,
                color = category.color,
                icon = category.icon,
                createdAt = category.createdAt,
                updatedAt = category.updatedAt
            )
        }
    }
}
