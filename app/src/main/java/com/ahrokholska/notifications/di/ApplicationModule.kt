package com.ahrokholska.notifications.di

import com.ahrokholska.notifications.data.PagesRepositoryImpl
import com.ahrokholska.notifications.domain.repositories.PagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindRepository(pagesRepository: PagesRepositoryImpl): PagesRepository
}