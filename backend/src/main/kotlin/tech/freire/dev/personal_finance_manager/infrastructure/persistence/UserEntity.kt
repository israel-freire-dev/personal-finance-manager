package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import jakarta.persistence.*
import tech.freire.dev.personal_finance_manager.domain.model.User
import java.util.UUID
import org.springframework.data.domain.Persistable

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @Column(name = "id")
    val _id: UUID,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String
) : BaseEntity(), Persistable<UUID> {

    @Transient
    override fun isNew(): Boolean = createdAt == null

    override fun getId(): UUID = _id

    /**
     * Converte entidade JPA para entidade de domínio.
     */
    fun toDomain(): User {
        return User(
            id = _id,
            name = name,
            email = email,
            passwordHash = passwordHash,
            createdAt = createdAt ?: java.time.LocalDateTime.now(),
            updatedAt = updatedAt ?: java.time.LocalDateTime.now()
        )
    }
}
