package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.LoginCommand
import tech.freire.dev.personal_finance_manager.application.response.AuthResponse

interface LoginUserInputPort {
    fun execute(command: LoginCommand): AuthResponse
}
