package com.ahrokholska.notifications.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahrokholska.notifications.domain.useCases.AddPageUseCase
import com.ahrokholska.notifications.domain.useCases.GetPagesUseCase
import com.ahrokholska.notifications.domain.useCases.RemovePageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getPagesUseCase: GetPagesUseCase,
    private val addPageUseCase: AddPageUseCase,
    private val removePageUseCase: RemovePageUseCase,
) : ViewModel() {
    private val _pages = MutableStateFlow(listOf(1))
    val pages = _pages.asStateFlow()
    private val _pageToScrollTo = MutableStateFlow<Int?>(null)
    val pageToScrollTo = _pageToScrollTo.asStateFlow()

    init {
        viewModelScope.launch {
            getPagesUseCase()?.let { pages ->
                _pages.update { pages }
            }
        }
    }

    fun addPage(onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            var addedPage = 0
            _pages.update {
                addedPage = it.last() + 1
                it + addedPage
            }
            onSuccess(addedPage)
            addPageUseCase()
        }
    }

    fun removePage(onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            var removedPage = 0
            _pages.update {
                removedPage = it.last()
                it.toMutableList().apply { removeLast() }
            }
            onSuccess(removedPage)
            removePageUseCase()
        }
    }

    fun updatePageToScrollTo(page: Int) {
        viewModelScope.launch {
            _pageToScrollTo.update { page }
        }
    }

    fun scrollCompleted() {
        viewModelScope.launch {
            _pageToScrollTo.update { null }
        }
    }
}