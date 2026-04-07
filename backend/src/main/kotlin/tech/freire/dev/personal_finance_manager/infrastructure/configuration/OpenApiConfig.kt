package tech.freire.dev.personal_finance_manager.infrastructure.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 * Acessível em /swagger-ui/index.html
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Personal Finance Manager API")
                    .description("API para gerenciamento de finanças pessoais. Controle transações, categorias, templates recorrentes e resumos mensais.")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Israel Freire")
                            .url("https://freire.dev")
                    )
                    .license(
                        License()
                            .name("MIT")
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url("http://localhost:8080")
                        .description("Servidor de Desenvolvimento")
                )
            )
    }
}
