package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateUserCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateUserCommand
import tech.freire.dev.personal_finance_manager.application.response.UserResponse
import java.util.UUID

interface UserCrudInputPort {
    fun findById(id: UUID): UserResponse?
    fun findByEmail(email: String): UserResponse?
    fun update(id: UUID, command: UpdateUserCommand): UserResponse
    fun delete(id: UUID)
}
