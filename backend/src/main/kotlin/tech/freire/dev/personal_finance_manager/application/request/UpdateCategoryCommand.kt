package tech.freire.dev.personal_finance_manager.application.request

data class UpdateCategoryCommand(
    val name: String,
    val color: String? = null,
    val icon: String? = null
)
