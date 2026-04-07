package tech.freire.dev.personal_finance_manager.application.response

import tech.freire.dev.personal_finance_manager.domain.model.User
import java.time.LocalDateTime

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(user: User): UserResponse {
            return UserResponse(
                id = user.id.toString(),
                name = user.name,
                email = user.email,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}
