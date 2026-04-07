package tech.freire.dev.personal_finance_manager.application.request

import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import java.util.UUID

data class CreateCategoryCommand(
    val userId: UUID,
    val parentId: UUID? = null,
    val name: String,
    val type: TransactionType,
    val color: String? = null,
    val icon: String? = null
)
