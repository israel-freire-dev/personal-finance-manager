package tech.freire.dev.personal_finance_manager.infrastructure.persistence

import org.springframework.stereotype.Component
import tech.freire.dev.personal_finance_manager.domain.model.MonthlySummary
import tech.freire.dev.personal_finance_manager.domain.repository.MonthlySummaryRepository
import java.util.UUID

@Component
class MonthlySummaryJpaAdapter(
    private val jpaRepository: SpringMonthlySummaryJpaRepository,
    private val userRepository: SpringUserJpaRepository
) : MonthlySummaryRepository {

    override fun save(summary: MonthlySummary): MonthlySummary {
        val user = userRepository.findById(summary.userId)
            .orElseThrow { IllegalStateException("User not found: ${summary.userId}") }

        val entity = MonthlySummaryEntity.fromDomain(summary, user)
        return jpaRepository.save(entity).toDomain()
    }

    override fun findById(id: UUID): MonthlySummary? {
        return jpaRepository.findById(id).map { it.toDomain() }.orElse(null)
    }

    override fun findByUserIdAndMonthYear(userId: UUID, month: Int, year: Int): MonthlySummary? {
        return jpaRepository.findByUserIdAndMonthAndYear(userId, month, year)?.toDomain()
    }

    override fun findAllByUserId(userId: UUID): List<MonthlySummary> {
        return jpaRepository.findAllByUserId(userId).map { it.toDomain() }
    }
}
