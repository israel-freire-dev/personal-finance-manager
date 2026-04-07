package tech.freire.dev.personal_finance_manager.infrastructure.worker

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tech.freire.dev.personal_finance_manager.application.usecase.ProcessRecurringTransactionsInputPort
import java.time.LocalDate

@Component
class RecurringTransactionWorker(
    private val processRecurringTransactionsInputPort: ProcessRecurringTransactionsInputPort
) {
    private val log = LoggerFactory.getLogger(RecurringTransactionWorker::class.java)

    /**
     * Roda todos os dias às 2 da manhã
     */
    @Scheduled(cron = "0 0 2 * * *")
    fun process() {
        log.info("Iniciando processo de geração de transações recorrentes...")
        processRecurringTransactionsInputPort.execute(LocalDate.now())
        log.info("Processo finalizado.")
    }
}
