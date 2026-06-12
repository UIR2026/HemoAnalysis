package ru.tanexc.hemoanalysis.navigation

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin

fun CoroutineScope.withLifecycle(lifecycle: Lifecycle): CoroutineScope {
    lifecycle.doOnDestroy(::cancel)
    return this
}

inline fun <reified T : BaseComponent> getComponent(vararg parameters: Any?) = getKoin().get<T> {
    parametersOf(*parameters)
}