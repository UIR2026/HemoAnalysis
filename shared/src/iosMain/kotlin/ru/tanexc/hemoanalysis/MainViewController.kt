package ru.tanexc.hemoanalysis

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import ru.tanexc.hemoanalysis.di.koinInit
import ru.tanexc.hemoanalysis.navigation.provideRootComponent

fun MainViewController(): UIViewController {
    koinInit()
    val root = provideRootComponent()
    return ComposeUIViewController { App(root) }
}
