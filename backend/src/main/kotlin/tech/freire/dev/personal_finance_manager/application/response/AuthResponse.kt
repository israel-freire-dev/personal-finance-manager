package tech.freire.dev.personal_finance_manager.application.response

data class AuthResponse(
    val token: String,
    val userId: String,
    val name: String,
    val email: String
)
