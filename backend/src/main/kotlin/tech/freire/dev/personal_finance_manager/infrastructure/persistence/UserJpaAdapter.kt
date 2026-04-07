package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import org.springframework.stereotype.Component
import tech.freire.dev.personal_finance_manager.domain.model.User
import tech.freire.dev.personal_finance_manager.domain.repository.UserRepository
import java.util.UUID

@Component
class UserJpaAdapter(
    private val jpaRepository: SpringUserJpaRepository
) : UserRepository {

    override fun save(user: User): User {
        val entity = UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            passwordHash = user.passwordHash
        )
        return jpaRepository.save(entity).toDomain()
    }

    override fun findById(id: UUID): User? {
        return jpaRepository.findById(id).map { it.toDomain() }.orElse(null)
    }

    override fun findByEmail(email: String): User? {
        return jpaRepository.findByEmail(email)?.toDomain()
    }

    override fun delete(id: UUID) {
        jpaRepository.deleteById(id)
    }
}
