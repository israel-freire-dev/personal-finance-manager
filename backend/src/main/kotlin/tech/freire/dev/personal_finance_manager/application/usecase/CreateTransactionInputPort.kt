package tech.freire.dev.personal_finance_manager.application.usecase

import tech.freire.dev.personal_finance_manager.application.request.CreateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.response.TransactionResponse

/**
 * Port de entrada para o caso de uso de criação de transação.
 * Define o contrato que o Use Case deve implementar.
 */
interface CreateTransactionInputPort {
    fun execute(command: CreateTransactionCommand): TransactionResponse
}
