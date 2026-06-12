package ru.tanexc.hemoanalysis.core.database

import com.russhwolf.settings.Settings
import org.koin.core.scope.Scope

internal expect fun Scope.createModelInfoSettings(): Settings