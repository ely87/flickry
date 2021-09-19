package com.flickry.android.core.usecases

import com.flickry.android.core.data.repo.FeedRepoResult
import com.flickry.android.core.data.repo.FeedRepository
import com.flickry.android.core.domain.FeedItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "GetFeedByTagsUseCase"

class GetFeedByTagsUseCase @Inject constructor(
    private val repo: FeedRepository,
    private val getDeviceLanguage: GetDeviceLanguageUseCase = GetDeviceLanguageUseCase(),
    private val bgDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(tags: String): Flow<Result> = withContext(bgDispatcher) {
        flowOf(Result.Loading)
        try {
            val tagResult = when (val result = repo.getSearchFeedItems(tags, getDeviceLanguage())) {
                is FeedRepoResult.Success -> {
                    Result.Loaded(result.items.map {
                        FeedItem(
                            title = it.title,
                            link = it.link,
                            media = it.media.link,
                            description = it.description,
                            published = it.published,
                            author = it.author,
                            tags = it.tags
                        )
                    })
                }
                is FeedRepoResult.Error -> {
                    Result.Error(result.error)
                }
            }

            return@withContext flowOf(tagResult)
        } catch (exception: Exception) {
            return@withContext flowOf(Result.Error(Exception("$TAG unknown error")))
        }
    }

    sealed class Result {
        object Loading : Result()
        data class Loaded(val items: List<FeedItem>) : Result()
        data class Error(val exception: Exception) : Result()
    }
}