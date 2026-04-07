package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.RegisterUserCommand
import tech.freire.dev.personal_finance_manager.application.response.AuthResponse

interface RegisterUserInputPort {
    fun execute(command: RegisterUserCommand): AuthResponse
}
