package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.LoginCommand
import tech.freire.dev.personal_finance_manager.application.response.AuthResponse
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.port.JwtProvider
import tech.freire.dev.personal_finance_manager.domain.port.PasswordHasher
import tech.freire.dev.personal_finance_manager.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val jwtProvider: JwtProvider
) : LoginUserInputPort {

    override fun execute(command: LoginCommand): AuthResponse {
        val user = userRepository.findByEmail(command.email)
            ?: throw DomainException("Invalid email or password") // avoid saying which one is wrong

        if (!passwordHasher.verify(command.password, user.passwordHash)) {
            throw DomainException("Invalid email or password")
        }

        val token = jwtProvider.generateToken(user.id, user.email)

        return AuthResponse(
            token = token,
            userId = user.id.toString(),
            name = user.name,
            email = user.email
        )
    }
}
