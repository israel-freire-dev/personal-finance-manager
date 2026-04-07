package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateCategoryCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateCategoryCommand
import tech.freire.dev.personal_finance_manager.application.response.CategoryResponse
import tech.freire.dev.personal_finance_manager.domain.model.Category
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class CategoryCrudUseCase(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository
) : CategoryCrudInputPort {

    override fun create(command: CreateCategoryCommand): CategoryResponse {
        // Validate user existence
        userRepository.findById(command.userId)
            ?: throw DomainException("User not found with id: ${command.userId}")

        // Validate parent category if informed
        if (command.parentId != null) {
            val parent = categoryRepository.findById(command.parentId)
                ?: throw DomainException("Parent category not found with id: ${command.parentId}")
            
            if (parent.userId != command.userId) {
                throw DomainException("Parent category belongs to another user")
            }
        }

        val now = LocalDateTime.now()
        val category = Category(
            id = UUID.randomUUID(),
            userId = command.userId,
            parentId = command.parentId,
            name = command.name,
            type = command.type,
            color = command.color,
            icon = command.icon,
            createdAt = now,
            updatedAt = now
        )

        val saved = categoryRepository.save(category)
        return CategoryResponse.fromDomain(saved)
    }

    override fun findById(id: UUID): CategoryResponse? {
        val category = categoryRepository.findById(id) ?: return null
        return CategoryResponse.fromDomain(category)
    }

    override fun findAllByUserId(userId: UUID): List<CategoryResponse> {
        val categories = categoryRepository.findAllByUserId(userId)
        return categories.map { CategoryResponse.fromDomain(it) }
    }

    override fun update(id: UUID, command: UpdateCategoryCommand): CategoryResponse {
        val category = categoryRepository.findById(id)
            ?: throw DomainException("Category not found with id: $id")

        val updatedCategory = category.copy(
            name = command.name,
            color = command.color,
            icon = command.icon,
            updatedAt = LocalDateTime.now()
        )

        val saved = categoryRepository.save(updatedCategory)
        return CategoryResponse.fromDomain(saved)
    }

    override fun delete(id: UUID) {
        val category = categoryRepository.findById(id)
            ?: throw DomainException("Category not found with id: $id")
        categoryRepository.delete(category.id)
    }
}
