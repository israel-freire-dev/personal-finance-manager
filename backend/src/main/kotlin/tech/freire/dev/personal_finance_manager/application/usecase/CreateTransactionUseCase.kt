package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.response.TransactionResponse
import tech.freire.dev.personal_finance_manager.application.usecase.CreateTransactionInputPort
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import java.time.LocalDateTime
import java.util.UUID

/**
 * Use Case: Criar uma nova transação financeira.
 *
 * Regras:
 * - A categoria informada deve existir
 * - A categoria deve pertencer ao mesmo usuário
 * - Validações de domínio (amount > 0, description não vazia) são aplicadas pela entity
 * - Suporta definir status (PAID, PENDING, etc.) e vincular a uma categoria
 */
class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : CreateTransactionInputPort {

    override fun execute(command: CreateTransactionCommand): TransactionResponse {
        // 1. Buscar e validar categoria
        val category = categoryRepository.findById(command.categoryId)
            ?: throw DomainException("Category not found with id: ${command.categoryId}")

        // 2. Validar que a categoria pertence ao mesmo usuário
        if (category.userId != command.userId) {
            throw DomainException("Category does not belong to the specified user")
        }

        // 3. Criar entidade de domínio (validações do init{} são aplicadas automaticamente)
        val now = LocalDateTime.now()
        val transaction = Transaction(
            id = UUID.randomUUID(),
            userId = command.userId,
            categoryId = command.categoryId,
            recurringTemplateId = command.recurringTemplateId,
            description = command.description,
            amount = command.amount,
            date = command.date,
            status = command.status,
            type = command.type,
            createdAt = now,
            updatedAt = now
        )

        // 4. Persistir via repositório
        val saved = transactionRepository.save(transaction)

        // 5. Retornar DTO de resposta
        return TransactionResponse.fromDomain(saved)
    }
}
