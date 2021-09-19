package com.flickry.android.presentation.feed.main.adapter

import android.view.View
import com.flickry.android.R
import com.flickry.android.databinding.ListItemFeedBinding
import com.flickry.android.presentation.base.ImageLoader.load
import com.flickry.android.presentation.feed.main.FeedUiMapper.FeedItemUi
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem

internal class FeedAdapterItem(internal val feedItem: FeedItemUi) :
    BindableItem<ListItemFeedBinding>() {

    override fun bind(viewBinding: ListItemFeedBinding, position: Int) {
        viewBinding.feedElementTitle.text = feedItem.title
        viewBinding.feedElementImage.load(
            url = feedItem.imageUrl,
            placeholderIcon = feedItem.placeholderImage
        )
    }

    override fun getLayout(): Int = R.layout.list_item_feed

    override fun initializeViewBinding(view: View): ListItemFeedBinding =
        ListItemFeedBinding.bind(view)

    override fun isSameAs(other: Item<*>): Boolean {
        return other is FeedAdapterItem
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        return this.feedItem.tags == (other as FeedAdapterItem).feedItem.tags &&
                this.feedItem.author == other.feedItem.author &&
                this.feedItem.link == other.feedItem.link
    }

}