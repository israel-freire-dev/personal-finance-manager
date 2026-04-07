package tech.freire.dev.personal_finance_manager.application.request

data class RegisterUserCommand(
    val name: String,
    val email: String,
    val password: String
)
