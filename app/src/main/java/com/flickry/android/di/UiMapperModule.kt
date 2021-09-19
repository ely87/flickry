package com.flickry.android.di

import com.flickry.android.presentation.feed.main.FeedUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UiMapperModule {

    @Provides
    fun provideFeedUiMapper(): FeedUiMapper = FeedUiMapper()
}