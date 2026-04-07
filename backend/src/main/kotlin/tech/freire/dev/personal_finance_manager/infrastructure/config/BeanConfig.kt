package tech.freire.dev.personal_finance_manager.infrastructure.config

import tech.freire.dev.personal_finance_manager.application.usecase.CreateTransactionUseCase
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuração de injeção de dependência.
 * Registra os use cases como beans Spring, injetando os ports (abstrações).
 */
@Configuration
class BeanConfig {

    @Bean
    fun createTransactionUseCase(
        transactionRepository: TransactionRepository,
        categoryRepository: CategoryRepository
    ) = CreateTransactionUseCase(transactionRepository, categoryRepository)
}
