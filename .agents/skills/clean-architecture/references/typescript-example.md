# Exemplo Completo — TypeScript / Node.js

Feature de exemplo: **Criar Usuário**

---

## 1. Domain — Entity

```typescript
// src/domain/entities/User.ts

export class User {
  private constructor(
    public readonly id: string,
    public readonly name: string,
    public readonly email: string,
    public readonly passwordHash: string,
    public readonly createdAt: Date,
  ) {}

  static create(props: {
    id: string
    name: string
    email: string
    passwordHash: string
  }): User {
    if (!props.name || props.name.trim().length < 2) {
      throw new Error('Nome deve ter ao menos 2 caracteres')
    }
    if (!props.email.includes('@')) {
      throw new Error('E-mail inválido')
    }
    return new User(props.id, props.name, props.email, props.passwordHash, new Date())
  }
}
```

---

## 2. Application — Port (Interface do Repositório)

```typescript
// src/application/ports/repositories/IUserRepository.ts

import { User } from '@/domain/entities/User'

export interface IUserRepository {
  findByEmail(email: string): Promise<User | null>
  save(user: User): Promise<void>
}
```

## 2b. Application — Port (Interface de Serviço)

```typescript
// src/application/ports/services/IHashService.ts

export interface IHashService {
  hash(plain: string): Promise<string>
}
```

## 2c. Application — DTO

```typescript
// src/application/dtos/CreateUserDTO.ts

export interface CreateUserDTO {
  name: string
  email: string
  password: string
}

export interface CreateUserResponseDTO {
  id: string
  name: string
  email: string
}
```

## 2d. Application — Use Case

```typescript
// src/application/use-cases/CreateUserUseCase.ts

import { randomUUID } from 'crypto'
import { User } from '@/domain/entities/User'
import { IUserRepository } from '@/application/ports/repositories/IUserRepository'
import { IHashService } from '@/application/ports/services/IHashService'
import { CreateUserDTO, CreateUserResponseDTO } from '@/application/dtos/CreateUserDTO'

export class CreateUserUseCase {
  constructor(
    private readonly userRepository: IUserRepository,
    private readonly hashService: IHashService,
  ) {}

  async execute(dto: CreateUserDTO): Promise<CreateUserResponseDTO> {
    const existing = await this.userRepository.findByEmail(dto.email)
    if (existing) {
      throw new Error('E-mail já cadastrado')
    }

    const passwordHash = await this.hashService.hash(dto.password)

    const user = User.create({
      id: randomUUID(),
      name: dto.name,
      email: dto.email,
      passwordHash,
    })

    await this.userRepository.save(user)

    return { id: user.id, name: user.name, email: user.email }
  }
}
```

---

## 3. Adapters — Repository Implementation

```typescript
// src/adapters/repositories/PrismaUserRepository.ts

import { PrismaClient } from '@prisma/client'
import { User } from '@/domain/entities/User'
import { IUserRepository } from '@/application/ports/repositories/IUserRepository'

export class PrismaUserRepository implements IUserRepository {
  constructor(private readonly prisma: PrismaClient) {}

  async findByEmail(email: string): Promise<User | null> {
    const record = await this.prisma.user.findUnique({ where: { email } })
    if (!record) return null
    return User.create({ id: record.id, name: record.name, email: record.email, passwordHash: record.passwordHash })
  }

  async save(user: User): Promise<void> {
    await this.prisma.user.create({
      data: {
        id: user.id,
        name: user.name,
        email: user.email,
        passwordHash: user.passwordHash,
        createdAt: user.createdAt,
      },
    })
  }
}
```

## 3b. Adapters — Controller

```typescript
// src/adapters/controllers/UserController.ts

import { Request, Response } from 'express'
import { CreateUserUseCase } from '@/application/use-cases/CreateUserUseCase'

export class UserController {
  constructor(private readonly createUserUseCase: CreateUserUseCase) {}

  async create(req: Request, res: Response): Promise<Response> {
    try {
      const result = await this.createUserUseCase.execute(req.body)
      return res.status(201).json(result)
    } catch (err: any) {
      if (err.message === 'E-mail já cadastrado') {
        return res.status(409).json({ message: err.message })
      }
      return res.status(400).json({ message: err.message })
    }
  }
}
```

---

## 4. Infra — Dependency Injection Container

```typescript
// src/infra/container/index.ts

import { PrismaClient } from '@prisma/client'
import { BcryptHashService } from '@/infra/services/BcryptHashService'
import { PrismaUserRepository } from '@/adapters/repositories/PrismaUserRepository'
import { CreateUserUseCase } from '@/application/use-cases/CreateUserUseCase'
import { UserController } from '@/adapters/controllers/UserController'

const prisma = new PrismaClient()

// Repositories
const userRepository = new PrismaUserRepository(prisma)

// Services
const hashService = new BcryptHashService()

// Use Cases
const createUserUseCase = new CreateUserUseCase(userRepository, hashService)

// Controllers
export const userController = new UserController(createUserUseCase)
```

## 4b. Infra — Routes

```typescript
// src/infra/http/routes/userRoutes.ts

import { Router } from 'express'
import { userController } from '@/infra/container'

const router = Router()

router.post('/users', (req, res) => userController.create(req, res))

export { router as userRoutes }
```
