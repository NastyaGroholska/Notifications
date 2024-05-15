package com.ahrokholska.notifications.presentation.mainScreen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.ahrokholska.notifications.R
import com.ahrokholska.notifications.presentation.theme.NotificationsTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val applicationContext = LocalContext.current.applicationContext
    MainScreenContent(
        pages = viewModel.pages.collectAsState().value,
        onCreateClick = {},
        onRemoveClick = {
            viewModel.removePage {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.page_was_removed, it),
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onAddClick = viewModel::addPage
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    pages: List<Int>,
    onCreateClick: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    onAddClick: (onSuccess: (Int) -> Unit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold { scaffoldPadding ->
        val pagerState = rememberPagerState(pageCount = { pages.size })
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