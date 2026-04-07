package tech.freire.dev.personal_finance_manager.application.request

data class UpdateUserCommand(
    val name: String,
    val email: String
)
