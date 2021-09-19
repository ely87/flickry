package com.flickry.android.core.data.datasource.service

import com.flickry.android.core.data.datasource.model.FeedNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun searchFeed(
        @Query("tags") tags: String,
        @Query("tagmode") tagMode: String = DEFAULT_TAG_MODE,
        @Query("lang") language: String = DEFAULT_LANGUAGE_TAG
    ): FeedNetworkResponse

    companion object {
        private const val DEFAULT_TAG_MODE = "all"
        private const val DEFAULT_LANGUAGE_TAG = "en-us"
    }
}