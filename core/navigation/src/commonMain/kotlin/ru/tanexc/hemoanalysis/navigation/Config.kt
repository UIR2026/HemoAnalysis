package ru.tanexc.hemoanalysis.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Config(
    val navLabelResId: String,
    val navIconResId: String
) {

    @Serializable
    data object Analysis: Config(
        navLabelResId = "analysis_label",
        navIconResId = "analysis",
    )

    @Serializable
    data object Settings: Config(
        navLabelResId = "settings_label",
        navIconResId = "settings",
    )

    companion object {
        val navEntries: List<Config> = listOf(Analysis, Settings)
    }
}
