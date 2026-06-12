package ru.tanexc.hemoanalysis.navigation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun provideRootComponent(): RootComponent {
    return RootComponent(DefaultComponentContext(LifecycleRegistry()))
}