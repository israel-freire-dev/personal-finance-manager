package tech.freire.dev.personal_finance_manager.domain.port

import java.util.UUID

interface JwtProvider {
    fun generateToken(userId: UUID, email: String): String
    fun getUserIdFromToken(token: String): UUID
    fun validateToken(token: String): Boolean
}
