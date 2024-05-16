package com.ahrokholska.notifications.domain.useCases

import com.ahrokholska.notifications.domain.repositories.PagesRepository
import javax.inject.Inject

class AddPageUseCase @Inject constructor(private val pagesRepository: PagesRepository) {
    suspend operator fun invoke() = pagesRepository.addPage()
}