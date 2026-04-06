package tech.freire.dev.personal_finance_manager.domain.repository

import tech.freire.dev.personal_finance_manager.domain.model.User
import java.util.UUID

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun delete(id: UUID)
}
