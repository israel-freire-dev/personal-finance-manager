package tech.freire.dev.personal_finance_manager.infrastructure.security

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component
import tech.freire.dev.personal_finance_manager.domain.port.PasswordHasher

@Component
class Argon2PasswordHasher : PasswordHasher {

    private val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    override fun hash(rawPassword: String): String {
        return encoder.encode(rawPassword) ?: throw IllegalStateException("Encoding failed")
    }

    override fun verify(rawPassword: String, hashedPassword: String): Boolean {
        return encoder.matches(rawPassword, hashedPassword)
    }
}
