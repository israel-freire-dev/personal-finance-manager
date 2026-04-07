package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import jakarta.persistence.*
import tech.freire.dev.personal_finance_manager.domain.model.User
import java.util.UUID

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String
) : BaseEntity() {

    /**
     * Converte entidade JPA para entidade de domínio.
     */
    fun toDomain(): User {
        return User(
            id = id,
            name = name,
            email = email,
            passwordHash = passwordHash,
            createdAt = createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = updatedAt ?: java.time.LocalDateTime.now()
        )
    }
}
