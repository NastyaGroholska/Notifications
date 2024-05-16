package com.ahrokholska.notifications.presentation.mainScreen

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.core.app.NotificationCompat
import com.ahrokholska.notifications.R
import com.ahrokholska.notifications.presentation.Constants.CHANNEL_ID
import com.ahrokholska.notifications.presentation.Constants.PAGE_NUMBER
import com.ahrokholska.notifications.presentation.MainActivity
import com.ahrokholska.notifications.presentation.theme.NotificationsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val applicationContext = LocalContext.current.applicationContext
    val notificationManager = remember {
        applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    val postNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else null

    MainScreenContent(
        pages = viewModel.pages.collectAsState().value,
        onCreateClick = { pageNumber ->
            val remoteViews =
                RemoteViews(applicationContext.packageName, R.layout.custom_notification).apply {
                    setTextViewText(
                        R.id.avatarTextView,
                        applicationContext.getString(R.string.notification_avatar_placeholder)
                    )
                    setTextViewText(
                        R.id.title,
                        applicationContext.getString(R.string.notification_header)
                    )
                    setTextViewText(
                        R.id.contentText,
                        applicationContext.getString(R.string.notification_content_text, pageNumber)
                    )
                }
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setColor(Color.argb(100, 42, 86, 198))
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup("$pageNumber")
                .setContentIntent(
                    PendingIntent.getActivity(
                        applicationContext,
                        pageNumber,
                        Intent(applicationContext, MainActivity::class.java).apply {
                            putExtra(PAGE_NUMBER, pageNumber)
                        },
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                postNotificationPermission?.let {
                    if (!postNotificationPermission.status.isGranted) {
                        postNotificationPermission.launchPermissionRequest()
                    } else {
                        notificationManager.notify("$pageNumber", Random.nextInt(), builder.build())
                    }
                }
            } else {
                notificationManager.notify("$pageNumber", Random.nextInt(), builder.build())
            }
        },
        onRemoveClick = {
            viewModel.removePage {
                for (notification in notificationManager.activeNotifications) {
                    if (notification.tag == "$it") {
                        notificationManager.cancel("$it", notification.id)
                    }
                }
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.page_was_removed, it),
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onAddClick = viewModel::addPage,
        pageToScrollTo = viewModel.pageToScrollTo.collectAsState().value,
        onScrollCompleted = viewModel::scrollCompleted
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    pages: List<Int>,
    onCreateClick: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    onAddClick: (onSuccess: (Int) -> Unit) -> Unit,
    pageToScrollTo: Int? = null,
    onScrollCompleted: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold { scaffoldPadding ->
        val pagerState = rememberPagerState(pageCount = { pages.size })
        LaunchedEffect(key1 = pageToScrollTo) {
            pageToScrollTo?.let {
                pagerState.scrollToPage(it - 1)
                onScrollCompleted()
            }
        }
        HorizontalPager(state = pagerState) { pageIndex ->
            Page(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding),
                number = pages[pageIndex],
                onlyOnePageExists = pages.size == 1,
                onCreateClick = { onCreateClick(pages[pageIndex]) },
                onRemoveClick = onRemoveClick,
                onAddClick = {
                    onAddClick {
                        coroutineScope.launch {
                            snapshotFlow { pagerState.pageCount }.collect { pageCount ->
                                if (pageCount == it) {
                                    pagerState.scrollToPage(it - 1)
                                    cancel()
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MainScreenPreview() {
    NotificationsTheme {
        MainScreenContent(
            pages = listOf(1, 2),
            onCreateClick = {},
            onRemoveClick = {},
            onAddClick = {}
        )
    }
}