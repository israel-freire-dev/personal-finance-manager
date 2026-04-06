package tech.freire.dev.personal_finance_manager.dto

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    open val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    open val updatedAt: LocalDateTime? = null
)
