package com.ahrokholska.notifications.domain.useCases

import com.ahrokholska.notifications.domain.repositories.PagesRepository
import javax.inject.Inject

class GetPagesUseCase @Inject constructor(private val pagesRepository: PagesRepository) {
    suspend operator fun invoke() =
        pagesRepository.getLastPage()?.let { lastPage -> List(lastPage) { it + 1 } }
}