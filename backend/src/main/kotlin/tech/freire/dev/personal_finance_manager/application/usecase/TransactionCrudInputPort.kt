package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.UpdateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.response.TransactionResponse
import java.util.UUID

interface TransactionCrudInputPort {
    fun findById(id: UUID): TransactionResponse?
    fun findAllByUserId(userId: UUID): List<TransactionResponse>
    fun update(id: UUID, command: UpdateTransactionCommand): TransactionResponse
    fun delete(id: UUID)
}
