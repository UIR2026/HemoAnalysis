package ru.tanexc.hemoanalysis.navigation

import androidx.activity.ComponentActivity
import com.arkivanov.decompose.retainedComponent

fun ComponentActivity.provideRootComponent(): RootComponent = retainedComponent { context ->
    RootComponent(context)
}