package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.RegisterUserCommand
import tech.freire.dev.personal_finance_manager.application.response.AuthResponse
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.model.User
import tech.freire.dev.personal_finance_manager.domain.port.JwtProvider
import tech.freire.dev.personal_finance_manager.domain.port.PasswordHasher
import tech.freire.dev.personal_finance_manager.domain.repository.UserRepository
import java.time.LocalDateTime
import java.util.UUID

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val jwtProvider: JwtProvider
) : RegisterUserInputPort {

    override fun execute(command: RegisterUserCommand): AuthResponse {
        if (userRepository.findByEmail(command.email) != null) {
            throw DomainException("Email already in use")
        }

        if (command.password.isBlank()) {
            throw DomainException("Password cannot be blank")
        }

        val hashedPassword = passwordHasher.hash(command.password)
        val now = LocalDateTime.now()

        val user = User(
            id = UUID.randomUUID(),
            name = command.name,
            email = command.email,
            passwordHash = hashedPassword,
            createdAt = now,
            updatedAt = now
        )

        val savedUser = userRepository.save(user)
        val token = jwtProvider.generateToken(savedUser.id, savedUser.email)

        return AuthResponse(
            token = token,
            userId = savedUser.id.toString(),
            name = savedUser.name,
            email = savedUser.email
        )
    }
}
