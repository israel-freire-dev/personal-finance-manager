package tech.freire.dev.personal_finance_manager.application.port.input

import tech.freire.dev.personal_finance_manager.application.dto.CreateTransactionCommand
import tech.freire.dev.personal_finance_manager.application.dto.TransactionResponse

/**
 * Port de entrada para o caso de uso de criação de transação.
 * Define o contrato que o Use Case deve implementar.
 */
interface CreateTransactionInputPort {
    fun execute(command: CreateTransactionCommand): TransactionResponse
}
