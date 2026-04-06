package tech.freire.dev.personal_finance_manager.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
