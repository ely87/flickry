package com.flickry.android.di

import com.flickry.android.core.data.datasource.service.FeedService
import com.flickry.android.core.data.repo.FeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideFeedRepository(feedService: FeedService): FeedRepository =
        FeedRepository(feedService)
}