package tech.freire.dev.personal_finance_manager.application.request

data class CreateUserCommand(
    val name: String,
    val email: String,
    val passwordHash: String
)
