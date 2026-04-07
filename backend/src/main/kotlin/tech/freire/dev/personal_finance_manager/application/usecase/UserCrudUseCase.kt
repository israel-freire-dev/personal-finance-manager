package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateUserCommand
import tech.freire.dev.personal_finance_manager.application.request.UpdateUserCommand
import tech.freire.dev.personal_finance_manager.application.response.UserResponse
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.model.User
import tech.freire.dev.personal_finance_manager.domain.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class UserCrudUseCase(
    private val userRepository: UserRepository
) : UserCrudInputPort {

    override fun create(command: CreateUserCommand): UserResponse {
        // Validate if email already exists
        if (userRepository.findByEmail(command.email) != null) {
            throw DomainException("Email already in use")
        }

        val now = LocalDateTime.now()
        val user = User(
            id = UUID.randomUUID(),
            name = command.name,
            email = command.email,
            passwordHash = command.passwordHash,
            createdAt = now,
            updatedAt = now
        )

        val saved = userRepository.save(user)
        return UserResponse.fromDomain(saved)
    }

    override fun findById(id: UUID): UserResponse? {
        val user = userRepository.findById(id) ?: return null
        return UserResponse.fromDomain(user)
    }

    override fun findByEmail(email: String): UserResponse? {
        val user = userRepository.findByEmail(email) ?: return null
        return UserResponse.fromDomain(user)
    }

    override fun update(id: UUID, command: UpdateUserCommand): UserResponse {
        val user = userRepository.findById(id) ?: throw DomainException("User not found")

        // Validate email uniqueness if changed
        if (user.email != command.email) {
            if (userRepository.findByEmail(command.email) != null) {
                throw DomainException("Email already in use")
            }
        }

        val updatedUser = user.copy(
            name = command.name,
            email = command.email,
            updatedAt = LocalDateTime.now()
        )

        val saved = userRepository.save(updatedUser)
        return UserResponse.fromDomain(saved)
    }

    override fun delete(id: UUID) {
        val user = userRepository.findById(id) ?: throw DomainException("User not found")
        userRepository.delete(user.id)
    }
}
