package com.ahrokholska.notifications.domain.useCases

import com.ahrokholska.notifications.domain.repositories.PagesRepository
import javax.inject.Inject

class RemovePageUseCase @Inject constructor(private val pagesRepository: PagesRepository) {
    suspend operator fun invoke() = pagesRepository.removePage()
}