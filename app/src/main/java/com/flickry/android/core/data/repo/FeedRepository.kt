package com.flickry.android.core.data.repo

import com.flickry.android.core.data.datasource.model.FeedItemNetwork
import com.flickry.android.core.data.datasource.service.FeedService
import okio.IOException
import retrofit2.HttpException

class FeedRepository(private val service: FeedService) {

    suspend fun getSearchFeedItems(tags: String, language: String): FeedRepoResult {
        return try {
            val feedItems: List<FeedItemNetwork> = service.searchFeed(tags, language).items
            FeedRepoResult.Success(feedItems)
        } catch (exception: IOException) {
            FeedRepoResult.Error(exception)
        } catch (exception: HttpException) {
            FeedRepoResult.Error(exception)
        }
    }

}

sealed class FeedRepoResult {
    data class Success(val items: List<FeedItemNetwork>) : FeedRepoResult()
    data class Error(val error: Exception) : FeedRepoResult()
}