package tech.freire.dev.personal_finance_manager.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.freire.dev.personal_finance_manager.application.usecase.*
import tech.freire.dev.personal_finance_manager.domain.repository.*

/**
 * Configuração de injeção de dependência.
 * Registra os use cases como beans Spring, injetando os ports (abstrações).
 */
@Configuration
class BeanConfig {

    @Bean
    fun userCrudUseCase(
        userRepository: UserRepository
    ): UserCrudInputPort = UserCrudUseCase(userRepository)

    @Bean
    fun registerUserUseCase(
        userRepository: UserRepository,
        passwordHasher: tech.freire.dev.personal_finance_manager.domain.port.PasswordHasher,
        jwtProvider: tech.freire.dev.personal_finance_manager.domain.port.JwtProvider
    ): RegisterUserInputPort = RegisterUserUseCase(userRepository, passwordHasher, jwtProvider)

    @Bean
    fun loginUserUseCase(
        userRepository: UserRepository,
        passwordHasher: tech.freire.dev.personal_finance_manager.domain.port.PasswordHasher,
        jwtProvider: tech.freire.dev.personal_finance_manager.domain.port.JwtProvider
    ): LoginUserInputPort = LoginUserUseCase(userRepository, passwordHasher, jwtProvider)

    @Bean
    fun categoryCrudUseCase(
        categoryRepository: CategoryRepository,
        userRepository: UserRepository
    ): CategoryCrudInputPort = CategoryCrudUseCase(categoryRepository, userRepository)

    @Bean
    fun createTransactionUseCase(
        transactionRepository: TransactionRepository,
        categoryRepository: CategoryRepository,
        monthlySummaryRepository: MonthlySummaryRepository
    ): CreateTransactionInputPort = CreateTransactionUseCase(
        transactionRepository, categoryRepository, monthlySummaryRepository
    )

    @Bean
    fun transactionCrudUseCase(
        transactionRepository: TransactionRepository,
        categoryRepository: CategoryRepository,
        monthlySummaryRepository: MonthlySummaryRepository
    ): TransactionCrudInputPort = TransactionCrudUseCase(
        transactionRepository, categoryRepository, monthlySummaryRepository
    )

    @Bean
    fun createRecurringTemplateUseCase(
        recurringTemplateRepository: RecurringTemplateRepository,
        categoryRepository: CategoryRepository
    ): CreateRecurringTemplateInputPort = CreateRecurringTemplateUseCase(
        recurringTemplateRepository, categoryRepository
    )

    @Bean
    fun recurringTemplateCrudUseCase(
        recurringTemplateRepository: RecurringTemplateRepository
    ): RecurringTemplateCrudInputPort = RecurringTemplateCrudUseCase(recurringTemplateRepository)

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
