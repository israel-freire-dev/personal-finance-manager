package tech.freire.dev.personal_finance_manager.infrastructure.configuration

import tech.freire.dev.personal_finance_manager.application.usecase.CreateTransactionUseCase
import tech.freire.dev.personal_finance_manager.application.usecase.CreateTransactionInputPort
import tech.freire.dev.personal_finance_manager.application.usecase.ProcessRecurringTransactionsUseCase
import tech.freire.dev.personal_finance_manager.application.usecase.ProcessRecurringTransactionsInputPort
import tech.freire.dev.personal_finance_manager.application.usecase.DeleteRecurringTemplateUseCase
import tech.freire.dev.personal_finance_manager.application.usecase.GetMonthlySummaryUseCase
import tech.freire.dev.personal_finance_manager.application.usecase.GetMonthlySummaryInputPort
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.MonthlySummaryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.RecurringTemplateRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import tech.freire.dev.personal_finance_manager.domain.repository.UserRepository
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
        categoryRepository: CategoryRepository,
        monthlySummaryRepository: MonthlySummaryRepository
    ): CreateTransactionInputPort = CreateTransactionUseCase(
        transactionRepository, categoryRepository, monthlySummaryRepository
    )

    @Bean
    fun processRecurringTransactionsUseCase(
        recurringTemplateRepository: RecurringTemplateRepository,
        transactionRepository: TransactionRepository
    ): ProcessRecurringTransactionsInputPort {
        return ProcessRecurringTransactionsUseCase(recurringTemplateRepository, transactionRepository)
    }

    @Bean
    fun deleteRecurringTemplateUseCase(
        recurringTemplateRepository: RecurringTemplateRepository,
        transactionRepository: TransactionRepository
    ): DeleteRecurringTemplateUseCase {
        return DeleteRecurringTemplateUseCase(recurringTemplateRepository, transactionRepository)
    }

    @Bean
    fun getMonthlySummaryUseCase(
        monthlySummaryRepository: MonthlySummaryRepository
    ): GetMonthlySummaryInputPort {
        return GetMonthlySummaryUseCase(monthlySummaryRepository)
    }
}
