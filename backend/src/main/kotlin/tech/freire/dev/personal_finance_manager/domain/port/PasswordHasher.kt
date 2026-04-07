package tech.freire.dev.personal_finance_manager.domain.port

interface PasswordHasher {
    fun hash(rawPassword: String): String
    fun verify(rawPassword: String, hashedPassword: String): Boolean
}
