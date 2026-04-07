package tech.freire.dev.personal_finance_manager.application.request

data class LoginCommand(
    val email: String,
    val password: String
)
