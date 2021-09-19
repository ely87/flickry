package com.flickry.android.presentation.feed.main.adapter

import com.flickry.android.presentation.feed.main.FeedUiMapper.FeedItemUi
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section

internal class FeedAdapter : GroupAdapter<GroupieViewHolder>() {

    private val feedSection: Section = Section().apply {
        setPlaceholder(FeedAdapterPlaceholderItem())
        setHideWhenEmpty(true)
    }

    init {
        add(feedSection)
    }

    fun addFeedItems(items: List<FeedItemUi>) {
        feedSection.clear()
        feedSection.addAll(items.map { FeedAdapterItem(it) })
    }

}