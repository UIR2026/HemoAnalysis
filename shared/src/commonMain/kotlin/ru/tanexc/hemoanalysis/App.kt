package ru.tanexc.hemoanalysis

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.tanexc.hemoanalysis.navigation.RootComponent
import ru.tanexc.hemoanalysis.presentation.components.BottomNavigation
import ru.tanexc.hemoanalysis.presentation.theme.HemoAnalysis

@Composable
fun App(root: RootComponent) {
    HemoAnalysis {
        val currentScreen = root.childStack.subscribeAsState()

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    currentScreen = currentScreen.value.active.configuration,
                    onNavigate = root::onNavigate
                )
            },
        ) { paddingValues ->
            Children(
                modifier = Modifier.fillMaxSize(),
                stack = root.childStack,
                content = { child -> child.instance.Content(paddingValues) }
            )
        }
    }
}