package ru.tanexc.hemoanalysis.core.database

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.scope.Scope

internal actual fun Scope.createModelInfoSettings(): Settings {
    return SharedPreferencesSettings(
        delegate = get<Context>().getSharedPreferences("model_info_settings", Context.MODE_PRIVATE)
    )
}