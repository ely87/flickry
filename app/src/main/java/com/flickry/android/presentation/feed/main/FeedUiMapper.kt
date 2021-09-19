package com.flickry.android.presentation.feed.main

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import com.flickry.android.R
import com.flickry.android.core.domain.FeedItem
import kotlinx.parcelize.Parcelize

class FeedUiMapper {

    fun getLoadingState(): FeedUiProperties = FeedUiProperties.Loading(
        loadingVisible = true,
        feedListVisible = false,
        popularTagsVisible = false
    )

    fun getLoadedState(feedItems: List<FeedItem>, tags: List<String>?): FeedUiProperties =
        FeedUiProperties.Loaded(
            loadingVisible = false,
            feedListVisible = true,
            popularTagsVisible = true,
            feedItems = feedItems.mapToUi(),
            tags = tags.orEmpty()
        )

    fun getNoResultsState(): FeedUiProperties = FeedUiProperties.NoResult(
        loadingVisible = false,
        feedListVisible = true,
        popularTagsVisible = true,
        message = R.string.empty_results
    )

    sealed class FeedUiProperties(
        open val loadingVisible: Boolean,
        open val feedListVisible: Boolean,
        open val popularTagsVisible: Boolean
    ) {
        data class Loading(
            override val loadingVisible: Boolean,
            override val feedListVisible: Boolean,
            override val popularTagsVisible: Boolean
        ) : FeedUiProperties(loadingVisible, feedListVisible, popularTagsVisible)

        data class Loaded(
            override val loadingVisible: Boolean,
            override val feedListVisible: Boolean,
            override val popularTagsVisible: Boolean,
            val feedItems: List<FeedItemUi>,
            val tags: List<String>
        ) : FeedUiProperties(loadingVisible, feedListVisible, popularTagsVisible)

        data class NoResult(
            override val loadingVisible: Boolean,
            override val feedListVisible: Boolean,
            override val popularTagsVisible: Boolean,
            @StringRes val message: Int
        ) : FeedUiProperties(loadingVisible, feedListVisible, popularTagsVisible)

    }

    /**
     * UI class wrapper of [FeedItem]
     */
    @Parcelize
    data class FeedItemUi(
        val title: String,
        val description: String,
        val imageUrl: String,
        val link: String,
        val author: String,
        val tags: String,
        @DrawableRes val placeholderImage: Int
    ) : Parcelable
}

@VisibleForTesting
fun List<FeedItem>.mapToUi() = map { item ->
    FeedUiMapper.FeedItemUi(
        title = item.title,
        description = item.description,
        imageUrl = item.media,
        link = item.link,
        author = item.author,
        tags = item.tags.orEmpty(),
        placeholderImage = R.drawable.feed_item_placeholder_background
    )
}