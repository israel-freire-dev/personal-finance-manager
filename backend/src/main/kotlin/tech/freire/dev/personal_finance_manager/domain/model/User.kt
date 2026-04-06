package tech.freire.dev.personal_finance_manager.domain.model

import java.time.LocalDateTime
import java.util.UUID

import tech.freire.dev.personal_finance_manager.domain.exception.DomainException

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        if (name.isBlank()) {
            throw DomainException("User name cannot be blank")
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)\$".toRegex())) {
            throw DomainException("Invalid user email format")
        }
    }
}
