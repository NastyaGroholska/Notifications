package com.ahrokholska.notifications.presentation.mainScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ahrokholska.notifications.presentation.theme.NotificationsTheme

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    MainScreenContent()
}

@Composable
fun MainScreenContent() {
    Scaffold { scaffoldPadding ->
        Page(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            number = 1,
            onlyOnePageExists = false,
            onCreateClick = {},
            onRemoveClick = {},
            onAddClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun MainScreenPreview() {
    NotificationsTheme {
        MainScreenContent()
    }
}