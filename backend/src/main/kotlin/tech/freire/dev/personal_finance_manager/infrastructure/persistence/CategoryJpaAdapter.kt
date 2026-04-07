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
    private val jpaRepository: SpringCategoryJpaRepository,
    private val userJpaRepository: SpringUserJpaRepository
) : CategoryRepository {

    override fun save(category: Category): Category {
        val userEntity = userJpaRepository.findById(category.userId)
            .orElseThrow { IllegalStateException("User not found in database: ${category.userId}") }
            
        val parentEntity = category.parentId?.let {
            jpaRepository.findById(it).orElseThrow { 
                IllegalStateException("Parent category not found: it") 
            }
        }

        val entity = CategoryEntity(
            id = category.id,
            user = userEntity,
            parent = parentEntity,
            name = category.name,
            type = category.type,
            color = category.color,
            icon = category.icon
        )
        
        return jpaRepository.save(entity).toDomain()
    }

    override fun findById(id: UUID): Category? {
        return jpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAllByUserId(userId: UUID): List<Category> {
        return jpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }

    override fun delete(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
