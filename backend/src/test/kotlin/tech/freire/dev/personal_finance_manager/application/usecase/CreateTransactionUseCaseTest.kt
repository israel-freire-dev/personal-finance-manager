package tech.freire.dev.personal_finance_manager.application.usecase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import tech.freire.dev.personal_finance_manager.application.request.CreateTransactionCommand
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionStatus
import tech.freire.dev.personal_finance_manager.domain.enums.TransactionType
import tech.freire.dev.personal_finance_manager.domain.model.DomainException
import tech.freire.dev.personal_finance_manager.domain.model.Category
import tech.freire.dev.personal_finance_manager.domain.model.Transaction
import tech.freire.dev.personal_finance_manager.domain.repository.CategoryRepository
import tech.freire.dev.personal_finance_manager.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@DisplayName("CreateTransactionUseCase")
class CreateTransactionUseCaseTest {

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var useCase: CreateTransactionUseCase

    private val userId = UUID.randomUUID()
    private val categoryId = UUID.randomUUID()
    private val now = LocalDateTime.now()

    private val validCategory = Category(
        id = categoryId,
        userId = userId,
        parentId = null,
        name = "Alimentação",
        type = TransactionType.EXPENSE,
        color = "#FF5733",
        icon = "food",
        createdAt = now,
        updatedAt = now
    )

    @BeforeEach
    fun setUp() {
        transactionRepository = mock()
        categoryRepository = mock()
        useCase = CreateTransactionUseCase(transactionRepository, categoryRepository)
    }

    private fun validCommand(
        status: TransactionStatus = TransactionStatus.PAID,
        type: TransactionType = TransactionType.EXPENSE,
        amount: BigDecimal = BigDecimal("150.50"),
        description: String = "Compra supermercado",
        recurringTemplateId: UUID? = null
    ) = CreateTransactionCommand(
        userId = userId,
        categoryId = categoryId,
        description = description,
        amount = amount,
        date = LocalDate.of(2026, 4, 6),
        status = status,
        type = type,
        recurringTemplateId = recurringTemplateId
    )

    @Nested
    @DisplayName("Cenários de sucesso")
    inner class SuccessScenarios {

        @Test
        @DisplayName("Deve criar transação com status PAID e retornar response correto")
        fun shouldCreateTransactionWithPaidStatus() {
            // Arrange
            val command = validCommand(status = TransactionStatus.PAID)
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)
            whenever(transactionRepository.save(any())).thenAnswer { invocation ->
                invocation.getArgument<Transaction>(0)
            }

            // Act
            val response = useCase.execute(command)

            // Assert
            assertNotNull(response.id)
            assertEquals(userId.toString(), response.userId)
            assertEquals(categoryId.toString(), response.categoryId)
            assertEquals("Compra supermercado", response.description)
            assertEquals(BigDecimal("150.50"), response.amount)
            assertEquals(LocalDate.of(2026, 4, 6), response.date)
            assertEquals(TransactionStatus.PAID, response.status)
            assertEquals(TransactionType.EXPENSE, response.type)
            assertNull(response.recurringTemplateId)
        }

        @Test
        @DisplayName("Deve criar transação com status PENDING")
        fun shouldCreateTransactionWithPendingStatus() {
            val command = validCommand(status = TransactionStatus.PENDING)
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)
            whenever(transactionRepository.save(any())).thenAnswer { it.getArgument<Transaction>(0) }

            val response = useCase.execute(command)

            assertEquals(TransactionStatus.PENDING, response.status)
        }

        @Test
        @DisplayName("Deve criar transação com status INVESTED")
        fun shouldCreateTransactionWithInvestedStatus() {
            val command = validCommand(status = TransactionStatus.INVESTED, type = TransactionType.EXPENSE)
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)
            whenever(transactionRepository.save(any())).thenAnswer { it.getArgument<Transaction>(0) }

            val response = useCase.execute(command)

            assertEquals(TransactionStatus.INVESTED, response.status)
        }

        @Test
        @DisplayName("Deve criar transação do tipo INCOME")
        fun shouldCreateIncomeTransaction() {
            val incomeCategory = validCategory.copy(type = TransactionType.INCOME)
            val command = validCommand(type = TransactionType.INCOME)
            whenever(categoryRepository.findById(categoryId)).thenReturn(incomeCategory)
            whenever(transactionRepository.save(any())).thenAnswer { it.getArgument<Transaction>(0) }

            val response = useCase.execute(command)

            assertEquals(TransactionType.INCOME, response.type)
        }

        @Test
        @DisplayName("Deve vincular recurringTemplateId quando informado")
        fun shouldLinkRecurringTemplateId() {
            val templateId = UUID.randomUUID()
            val command = validCommand(recurringTemplateId = templateId)
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)
            whenever(transactionRepository.save(any())).thenAnswer { it.getArgument<Transaction>(0) }

            val response = useCase.execute(command)

            assertEquals(templateId.toString(), response.recurringTemplateId)
        }

        @Test
        @DisplayName("Deve chamar save do repositório exatamente uma vez")
        fun shouldCallSaveOnce() {
            val command = validCommand()
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)
            whenever(transactionRepository.save(any())).thenAnswer { it.getArgument<Transaction>(0) }

            useCase.execute(command)

            verify(transactionRepository, times(1)).save(any())
        }

        @Test
        @DisplayName("Deve passar a transação correta para o repositório")
        fun shouldPassCorrectTransactionToRepository() {
            val command = validCommand()
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)
            whenever(transactionRepository.save(any())).thenAnswer { it.getArgument<Transaction>(0) }

            useCase.execute(command)

            argumentCaptor<Transaction>().apply {
                verify(transactionRepository).save(capture())
                val saved = firstValue
                assertEquals(userId, saved.userId)
                assertEquals(categoryId, saved.categoryId)
                assertEquals("Compra supermercado", saved.description)
                assertEquals(BigDecimal("150.50"), saved.amount)
                assertEquals(TransactionStatus.PAID, saved.status)
                assertEquals(TransactionType.EXPENSE, saved.type)
            }
        }
    }

    @Nested
    @DisplayName("Cenários de erro — validações do Use Case")
    inner class UseCaseValidationErrors {

        @Test
        @DisplayName("Deve lançar DomainException quando categoria não existe")
        fun shouldThrowWhenCategoryNotFound() {
            val command = validCommand()
            whenever(categoryRepository.findById(categoryId)).thenReturn(null)

            val exception = assertThrows<DomainException> {
                useCase.execute(command)
            }

            assertTrue(exception.message!!.contains("Category not found"))
            verify(transactionRepository, never()).save(any())
        }

        @Test
        @DisplayName("Deve lançar DomainException quando categoria pertence a outro usuário")
        fun shouldThrowWhenCategoryBelongsToAnotherUser() {
            val anotherUserId = UUID.randomUUID()
            val otherUserCategory = validCategory.copy(userId = anotherUserId)
            val command = validCommand()
            whenever(categoryRepository.findById(categoryId)).thenReturn(otherUserCategory)

            val exception = assertThrows<DomainException> {
                useCase.execute(command)
            }

            assertTrue(exception.message!!.contains("does not belong to the specified user"))
            verify(transactionRepository, never()).save(any())
        }
    }

    @Nested
    @DisplayName("Cenários de erro — validações do Domain Entity")
    inner class DomainValidationErrors {

        @Test
        @DisplayName("Deve lançar DomainException quando amount é zero")
        fun shouldThrowWhenAmountIsZero() {
            val command = validCommand(amount = BigDecimal.ZERO)
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)

            val exception = assertThrows<DomainException> {
                useCase.execute(command)
            }

            assertTrue(exception.message!!.contains("amount must be greater than zero"))
            verify(transactionRepository, never()).save(any())
        }

        @Test
        @DisplayName("Deve lançar DomainException quando amount é negativo")
        fun shouldThrowWhenAmountIsNegative() {
            val command = validCommand(amount = BigDecimal("-50.00"))
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)

            val exception = assertThrows<DomainException> {
                useCase.execute(command)
            }

            assertTrue(exception.message!!.contains("amount must be greater than zero"))
            verify(transactionRepository, never()).save(any())
        }

        @Test
        @DisplayName("Deve lançar DomainException quando description está em branco")
        fun shouldThrowWhenDescriptionIsBlank() {
            val command = validCommand(description = "   ")
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)

            val exception = assertThrows<DomainException> {
                useCase.execute(command)
            }

            assertTrue(exception.message!!.contains("description cannot be blank"))
            verify(transactionRepository, never()).save(any())
        }

        @Test
        @DisplayName("Deve lançar DomainException quando description está vazia")
        fun shouldThrowWhenDescriptionIsEmpty() {
            val command = validCommand(description = "")
            whenever(categoryRepository.findById(categoryId)).thenReturn(validCategory)

            assertThrows<DomainException> {
                useCase.execute(command)
            }

            verify(transactionRepository, never()).save(any())
        }
    }
}
