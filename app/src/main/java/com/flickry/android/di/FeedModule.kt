package com.flickry.android.di

import com.flickry.android.core.data.datasource.service.FeedService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val SERVICE_BASE_URL = "https://api.flickr.com/"

/**
 * Singleton for object creation (data injection) handled by Hilt.
 * Hilt will use this information provided here to automatically make these dependencies
 * globally available.
 * @sample <a href="https://pizzo15.medium.com/get-started-with-mvvm-in-android-959e7666caa5">Hilt and MVVM</a>
 */
@Module
@InstallIn(SingletonComponent::class)
object FeedModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Singleton
    @Provides
    fun provideHttpLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(SERVICE_BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(provideHttpLogger())
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideFeedService(retrofit: Retrofit.Builder) =
        retrofit.build().create(FeedService::class.java)
}