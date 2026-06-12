package ru.tanexc.hemoanalysis.core.database

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope
import platform.Foundation.NSUserDefaults

internal actual fun Scope.createModelInfoSettings(): Settings {
    return NSUserDefaultsSettings(
        delegate = NSUserDefaults.standardUserDefaults
    )
}