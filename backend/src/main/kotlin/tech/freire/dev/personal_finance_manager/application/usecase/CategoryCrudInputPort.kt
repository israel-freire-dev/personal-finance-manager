package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateCategoryCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateCategoryCommand
import tech.freire.dev.personal_finance_manager.application.response.CategoryResponse
import java.util.UUID

interface CategoryCrudInputPort {
    fun create(command: CreateCategoryCommand): CategoryResponse
    fun findById(id: UUID): CategoryResponse?
    fun findAllByUserId(userId: UUID): List<CategoryResponse>
    fun update(id: UUID, command: UpdateCategoryCommand): CategoryResponse
    fun delete(id: UUID)
}
