package com.ahrokholska.notifications.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _pages = MutableStateFlow(listOf(1))
    val pages = _pages.asStateFlow()

    fun addPage(onSuccess: (Int) -> Unit) {
        viewModelScope.launch {
            var addedPage = 0
            _pages.update {
                addedPage = it.last() + 1
                it + addedPage
            }
            onSuccess(addedPage)
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
        }
    }
}