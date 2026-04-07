package tech.freire.dev.personal_finance_manager.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import tech.freire.dev.personal_finance_manager.application.response.MonthlySummaryResponse
import tech.freire.dev.personal_finance_manager.application.usecase.GetMonthlySummaryInputPort
import tech.freire.dev.personal_finance_manager.infrastructure.configuration.ErrorResponse
import java.util.UUID

/**
 * Controller REST para consulta de resumos financeiros mensais.
 */
@RestController
@RequestMapping("/api/v1/summaries")
@Tag(name = "Resumo Mensal", description = "Operações de consulta de fechamento mensal")
class MonthlySummaryController(
    private val getMonthlySummaryUseCase: GetMonthlySummaryInputPort
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Consultar resumo mensal",
        description = "Retorna o resumo financeiro de um mês para o usuário, incluindo saldo real e saldo previsto. Se o resumo ainda não existir, ele é criado automaticamente com rollover do mês anterior."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Resumo mensal retornado com sucesso",
                content = [Content(schema = Schema(implementation = MonthlySummaryResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Parâmetros inválidos",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun getSummary(
        @RequestParam userId: UUID,
        @RequestParam month: Int,
        @RequestParam year: Int
    ): MonthlySummaryResponse {
        return getMonthlySummaryUseCase.execute(userId, month, year)
    }
}
