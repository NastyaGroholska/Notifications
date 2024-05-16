package com.ahrokholska.notifications.domain.repositories

interface PagesRepository {
    suspend fun addPage()
    suspend fun removePage()
    suspend fun getLastPage(): Int?
}