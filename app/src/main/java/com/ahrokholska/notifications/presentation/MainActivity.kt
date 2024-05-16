package com.ahrokholska.notifications.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ahrokholska.notifications.presentation.Constants.PAGE_NUMBER
import com.ahrokholska.notifications.presentation.mainScreen.MainScreen
import com.ahrokholska.notifications.presentation.mainScreen.MainScreenViewModel
import com.ahrokholska.notifications.presentation.theme.NotificationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationsTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.extras?.getInt(PAGE_NUMBER)?.let {
            viewModel.updatePageToScrollTo(it)
        }
    }
}