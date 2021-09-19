package com.flickry.android.di

import com.flickry.android.core.data.repo.FeedRepository
import com.flickry.android.core.usecases.GetDeviceLanguageUseCase
import com.flickry.android.core.usecases.GetFeedByTagsUseCase
import com.flickry.android.core.usecases.GetPopularTagsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    fun provideGetFeedByTagsUseCase(feedRepository: FeedRepository): GetFeedByTagsUseCase =
        GetFeedByTagsUseCase(feedRepository)

    @Provides
    fun provideGetDeviceLanguageUseCase(): GetDeviceLanguageUseCase = GetDeviceLanguageUseCase()

    @Provides
    fun provideGetPopularTagsUseCase(): GetPopularTagsUseCase = GetPopularTagsUseCase()
}