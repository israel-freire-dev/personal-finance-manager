package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import tech.freire.dev.personal_finance_manager.domain.model.Category
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Adapter que implementa o port de repositório de categorias do domínio.
 * Traduz entre entidades de domínio e entidades JPA.
 */
@Component
class CategoryJpaAdapter(
    private val jpaRepository: SpringCategoryJpaRepository
) : CategoryRepository {

    override fun save(category: Category): Category {
        // Implementação completa será adicionada no use case de Category
        throw UnsupportedOperationException("Save category not yet implemented in this adapter")
    }

    override fun findById(id: UUID): Category? {
        return jpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAllByUserId(userId: UUID): List<Category> {
        return jpaRepository.findAll()
            .filter { it.user.id == userId }
            .map { it.toDomain() }
    }

    override fun delete(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
