package com.flickry.android.core.usecases

import com.flickry.android.core.domain.FeedItem

/**
 * Use case that randomly selects 5 elements from the list of feed items that are not null or empty.
 * Each tag is also limited to 5 chars. This is because the API is kind of weird and for some existing
 * tags, there are no results.
 * The final selected ones are then mapped into strings to be considered a "POPULAR TAGS".
 */
class GetPopularTagsUseCase {

    operator fun invoke(feedItems: List<FeedItem>): List<String> = feedItems
        .filterNot { it.tags.isNullOrEmpty() }
        .distinct()
        .shuffled()
        .mapNotNull { it.tags?.substring(0, it.tags.length.coerceAtMost(5)) }
        .take(feedItems.size.coerceAtMost(5))

}