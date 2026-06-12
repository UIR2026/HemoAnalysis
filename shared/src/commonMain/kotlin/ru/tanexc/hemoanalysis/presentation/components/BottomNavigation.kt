package ru.tanexc.hemoanalysis.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.navigation.Config
import ru.tanexc.hemoanalysis.util.getDrawableRes
import ru.tanexc.hemoanalysis.util.getStringRes

@Composable
fun BottomNavigation(
    currentScreen: Config,
    onNavigate: (Config) -> Unit
) {
    NavigationBar {
        Config.navEntries.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(getDrawableRes(screen.navIconResId)),
                        contentDescription = "lectures"
                    )
                },
                label = {
                    Text(
                        text = stringResource(getStringRes(screen.navLabelResId)),
                        fontSize = 10.sp,
                        fontWeight = Bold
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                ),
                onClick = {
                    onNavigate(screen)
                }
            )
        }

    }
}
