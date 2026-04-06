package tech.freire.dev.personal_finance_manager.domain.exception

open class DomainException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
