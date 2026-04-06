# Exemplo Completo — Kotlin / Spring Boot

Feature de exemplo: **Criar Usuário**

---

## 1. Domain — Entity

```kotlin
// src/main/kotlin/domain/entity/User.kt

package com.example.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(name.trim().length >= 2) { "Nome deve ter ao menos 2 caracteres" }
        require(email.contains("@")) { "E-mail inválido" }
    }
}
```

---

## 2. Application — Ports

```kotlin
// src/main/kotlin/application/port/output/UserRepositoryPort.kt

package com.example.application.port.output

import com.example.domain.entity.User
import java.util.UUID

interface UserRepositoryPort {
    fun findByEmail(email: String): User?
    fun save(user: User): User
}
```

```kotlin
// src/main/kotlin/application/port/output/HashServicePort.kt

package com.example.application.port.output

interface HashServicePort {
    fun hash(plain: String): String
}
```

```kotlin
// src/main/kotlin/application/port/input/CreateUserInputPort.kt

package com.example.application.port.input

import com.example.application.dto.CreateUserCommand
import com.example.application.dto.UserResponse

interface CreateUserInputPort {
    fun execute(command: CreateUserCommand): UserResponse
}
```

## 2b. Application — DTOs / Commands

```kotlin
// src/main/kotlin/application/dto/CreateUserCommand.kt

package com.example.application.dto

data class CreateUserCommand(
    val name: String,
    val email: String,
    val password: String
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String
)
```

## 2c. Application — Use Case

```kotlin
// src/main/kotlin/application/usecase/CreateUserUseCase.kt

package com.example.application.usecase

import com.example.application.dto.CreateUserCommand
import com.example.application.dto.UserResponse
import com.example.application.port.input.CreateUserInputPort
import com.example.application.port.output.HashServicePort
import com.example.application.port.output.UserRepositoryPort
import com.example.domain.entity.User
import java.util.UUID

class CreateUserUseCase(
    private val userRepository: UserRepositoryPort,
    private val hashService: HashServicePort
) : CreateUserInputPort {

    override fun execute(command: CreateUserCommand): UserResponse {
        val existing = userRepository.findByEmail(command.email)
        require(existing == null) { "E-mail já cadastrado" }

        val user = User(
            id = UUID.randomUUID(),
            name = command.name,
            email = command.email,
            passwordHash = hashService.hash(command.password)
        )

        val saved = userRepository.save(user)
        return UserResponse(id = saved.id.toString(), name = saved.name, email = saved.email)
    }
}
```

---

## 3. Adapters — Inbound (Controller)

```kotlin
// src/main/kotlin/adapter/inbound/web/UserController.kt

package com.example.adapter.inbound.web

import com.example.application.dto.CreateUserCommand
import com.example.application.dto.UserResponse
import com.example.application.port.input.CreateUserInputPort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val createUserUseCase: CreateUserInputPort
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateUserRequest): UserResponse {
        return createUserUseCase.execute(
            CreateUserCommand(
                name = request.name,
                email = request.email,
                password = request.password
            )
        )
    }
}

data class CreateUserRequest(val name: String, val email: String, val password: String)
```

## 3b. Adapters — Outbound (Repository Implementation)

```kotlin
// src/main/kotlin/adapter/outbound/persistence/UserJpaRepository.kt

package com.example.adapter.outbound.persistence

import com.example.application.port.output.UserRepositoryPort
import com.example.domain.entity.User
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserJpaAdapter(
    private val jpaRepository: SpringUserJpaRepository
) : UserRepositoryPort {

    override fun findByEmail(email: String): User? {
        return jpaRepository.findByEmail(email)?.toDomain()
    }

    override fun save(user: User): User {
        val entity = UserJpaEntity.fromDomain(user)
        return jpaRepository.save(entity).toDomain()
    }
}
```

---

## 4. Infrastructure — Bean Config (DI)

```kotlin
// src/main/kotlin/infrastructure/config/BeanConfig.kt

package com.example.infrastructure.config

import com.example.application.port.output.HashServicePort
import com.example.application.port.output.UserRepositoryPort
import com.example.application.usecase.CreateUserUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {

    @Bean
    fun createUserUseCase(
        userRepository: UserRepositoryPort,
        hashService: HashServicePort
    ) = CreateUserUseCase(userRepository, hashService)
}
```
