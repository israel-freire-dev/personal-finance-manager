package tech.freire.dev.personal_finance_manager.domain.repository

import tech.freire.dev.personal_finance_manager.domain.model.Category
import java.util.UUID

interface CategoryRepository {
    fun save(category: Category): Category
    fun findById(id: UUID): Category?
    fun findAllByUserId(userId: UUID): List<Category>
    fun delete(id: UUID)
}
