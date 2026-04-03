package tech.freire.dev.personal_finance_manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonalFinanceManagerApplication

fun main(args: Array<String>) {
	runApplication<PersonalFinanceManagerApplication>(*args)
}
